package agitter.controller.oauth;

import infra.logging.LogInfra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpSession;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.provider.GoogleImpl;

import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import utils.ReflectionUtils;
import agitter.controller.oauth.contactsimport.ContactsImport;
import agitter.domain.Agitter;
import agitter.domain.contacts.Contacts;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.Users.UserNotFound;

public class OAuth {

	public static final String TWITTER = "twitter";
	public static final String FACEBOOK = "facebook";
	public static final String YAHOO = "yahoo";
	public static final String HOTMAIL = "hotmail";
	public static final String GOOGLE = "google";
	
	private final Functor<EmailAddress, User> userProducer;
	private final Contacts contacts;
	
	public OAuth(Functor<EmailAddress, User> userProducer, Contacts contacts) {
		this.userProducer = userProducer;
		this.contacts = contacts;
		
		try {
			ReflectionUtils.setFinalStatic(GoogleImpl.class, "CONTACTS_FEED_URL", "http://www.google.com/m8/feeds/contacts/default/full/?max-results=9000");
		} catch (SecurityException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e);
		} catch (NoSuchFieldException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e);
		} catch (Exception e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e);
		}
	}
	
	public String googleSigninURL(String context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, GOOGLE);
	}
	public String windowsSigninURL(String context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, HOTMAIL);
	}
	public String yahooSigninURL(String context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, YAHOO);
	}
	public String facebookSigninURL(String context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, FACEBOOK);
	}
	public String twitterSigninURL(String context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, TWITTER);
	}
	
	public String googleLinkURL(String context, HttpSession httpSession) throws Exception {
		return linkURL(context, httpSession, GOOGLE);
	}
	public String windowsLinkURL(String context, HttpSession httpSession) throws Exception {
		return linkURL(context, httpSession, HOTMAIL);
	}
	public String yahooLinkURL(String context, HttpSession httpSession) throws Exception {
		return linkURL(context, httpSession, YAHOO);
	}
	public String facebookLinkURL(String context, HttpSession httpSession) throws Exception {
		return linkURL(context, httpSession, FACEBOOK);
	}
	public String twitterLinkURL(String context, HttpSession httpSession) throws Exception {
		return linkURL(context, httpSession, TWITTER);
	}
	
	
	public String signinURL(String context, HttpSession httpSession, String providerId) throws Exception {
		return callOAuth(context, "oauth", httpSession, providerId);
	}	

	public String linkURL(String context, HttpSession httpSession, String providerId) throws Exception {
		return callOAuth(context, "link", httpSession, providerId);
	}	
	
	private String callOAuth(String context, String retURL, HttpSession httpSession, String providerId) throws Exception {
		SocialAuthConfig config = SocialAuthConfig.getDefault();
		try {
			config.load();
	
			SocialAuthManager manager = new SocialAuthManager();
			manager.setSocialAuthConfig(config);
			
			String callbackUrl = context + retURL;
			String result = manager.getAuthenticationUrl(providerId, callbackUrl);

			httpSession.setAttribute("authManager", manager);
	
			return result;
		} catch (Exception e) {
			LogInfra.getLogger(this).severe("Erro de SocialAuth: " + e.getMessage());
			throw e;
		}
	}
	
	public User signinCallback(Map<String, String[]> params, HttpSession httpSession) throws Exception {
		SocialAuthManager manager = (SocialAuthManager) httpSession.getAttribute("authManager");
	
		Map<String, String> paramsMap = new HashMap<String, String>();
		for (String name : params.keySet())
			paramsMap.put(name, params.get(name)[0]);
		
		AuthProvider provider = null;
		Profile profile;
		try {
			provider = manager.connect(paramsMap);
			profile = provider.getUserProfile();
		} catch (Exception e) {
			LogInfra.getLogger(this).log(Level.SEVERE, "Erro no retorno do SocialAuth. Provider: " + provider + "\n" + e.getMessage());
			throw e;
		}
	
		User user = userProducer.evaluate(EmailAddress.email(profile.getEmail()));
		user.linkAccount(provider.getProviderId(), 
				 getUsername(profile), 
				 paramsMap.get("oauth_verifier") != null ? paramsMap.get("oauth_verifier") : paramsMap.get("exp"),
				 paramsMap.get("oauth_token") != null ? paramsMap.get("oauth_token") : paramsMap.get("wrap_verification_code"),
				 profile.getProfileImageURL(),
				 profile.getEmail() 
				 );					
		startContactImport(provider, user);
		return user;
	}

	public User linkAccountCallback(User user, Agitter domain, Map<String, String[]> params, HttpSession httpSession) throws Exception {
		SocialAuthManager manager = (SocialAuthManager) httpSession.getAttribute("authManager");
	
		Map<String, String> paramsMap = new HashMap<String, String>();
		for (String name : params.keySet()) 
			paramsMap.put(name, params.get(name)[0]);
		
		AuthProvider provider = null;
		Profile profile;
		try {
			provider = manager.connect(paramsMap);
			profile = provider.getUserProfile();
		} catch (Exception e) {
			LogInfra.getLogger(this).log(Level.SEVERE, "Erro no retorno do SocialAuth. Provider: " + provider + "\n" + e.getMessage());
			throw e;
		}
	
		user.linkAccount(provider.getProviderId(), 
						 getUsername(profile), 
						 paramsMap.get("oauth_verifier") != null ? paramsMap.get("oauth_verifier") : paramsMap.get("exp"),
						 paramsMap.get("oauth_token") != null ? paramsMap.get("oauth_token") : paramsMap.get("wrap_verification_code"),
						 profile.getProfileImageURL(),
						 profile.getEmail()
						 );
		startContactImport(provider, user);
		
		startMergingAccounts(domain, user.email().toString(), profile.getEmail());
		
		return user;
	}

	private void startMergingAccounts(final Agitter domain, final String email1, final String email2) {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					domain.joinAccounts(email1, email2);
				} catch (UserNotFound e) {
					e.printStackTrace();
				} catch (Refusal e) {
					e.printStackTrace();
				}
			}
		}; 
		t.run();
	}

	private void startContactImport(AuthProvider provider, User user) {
		if (!providesUsefulContacts(provider)) return;
		
		List<Contact> candidates;
		try {
			candidates = provider.getContactList();
		} catch (Exception e) {
			LogInfra.getLogger(this).severe("Erro importando contatos via SocialAuth. Provider: " + provider + "\n" + e.getMessage());
			return;
		}
		
		System.out.println("Importando " + candidates.size() + " contatos");
		
		char[] groupname = provider.getProviderId().toCharArray();
		groupname[0] = Character.toUpperCase(groupname[0]);
		
		if (candidates.isEmpty()) return;
		new ContactsImport(new String(groupname), contacts.contactsOf(user), candidates, userProducer)
			.start(); //Optimize: use a thread pool instead of starting tons of threads.

	}


	private boolean providesUsefulContacts(AuthProvider provider) {
		if (provider.getProviderId().equals(FACEBOOK)) return false;
		if (provider.getProviderId().equals(TWITTER)) return false;
		return true;
	}

	public String getUsername(Profile candidate) {	
		if (candidate.getDisplayName() != null) 
			return candidate.getDisplayName();

		if (candidate.getFullName() != null) 
			return candidate.getFullName();
		
		String name = "";
			
		if (candidate.getFirstName() != null)
			name = candidate.getFirstName() + " ";
		
		if (candidate.getLastName() != null) 
			name = name + candidate.getLastName();
		
		if (!name.trim().equals(""))
			return name.trim();
		
		if (candidate.getValidatedId() != null)
			return candidate.getValidatedId();
		
		return null;
	}
	
}
