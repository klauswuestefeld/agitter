package agitter.controller.oauth;

import static agitter.common.Portal.Facebook;
import static agitter.common.Portal.Google;
import static agitter.common.Portal.Twitter;
import static agitter.common.Portal.WindowsLive;
import static agitter.common.Portal.Yahoo;
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

import basis.lang.Functor;
import basis.lang.exceptions.Refusal;

import utils.ReflectionUtils;
import agitter.common.Portal;
import agitter.controller.oauth.contactsimport.ContactsImport;
import agitter.domain.Agitter;
import agitter.domain.contacts.Contacts;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.Users.UserNotFound;

public class OAuth {

	private final Functor<EmailAddress, User> userProducer;
	private final Contacts contacts;

	public OAuth(Functor<EmailAddress, User> userProducer, Contacts contacts) {
		this.userProducer = userProducer;
		this.contacts = contacts;
		
		try {
			ReflectionUtils.setFinalStatic(GoogleImpl.class, "CONTACTS_FEED_URL", "http://www.google.com/m8/feeds/contacts/default/full/?max-results=9000");
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	

	public String signinURL(String context, HttpSession httpSession, Portal portal) throws Exception {
		return callOAuth(context, "oauth", httpSession, portal);
	}	

	public String linkURL(String context, HttpSession httpSession, Portal portal) throws Exception {
		return callOAuth(context, "link", httpSession, portal);
	}	
	
	
	private String callOAuth(String context, String retURL, HttpSession httpSession, Portal portal) throws Exception {
		SocialAuthConfig config = SocialAuthConfig.getDefault();
		try {
			config.load();
	
			SocialAuthManager manager = new SocialAuthManager();
			manager.setSocialAuthConfig(config);
			httpSession.setAttribute("authManager", manager);
			
			String callbackUrl = context + retURL;
			return manager.getAuthenticationUrl(idFor(portal), callbackUrl);
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
		user.linkAccount(portalFor(provider), 
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
	
		user.linkAccount(portalFor(provider), 
						 getUsername(profile), 
						 paramsMap.get("oauth_verifier") != null ? paramsMap.get("oauth_verifier") : paramsMap.get("exp"),
						 paramsMap.get("oauth_token") != null ? paramsMap.get("oauth_token") : paramsMap.get("wrap_verification_code"),
						 profile.getProfileImageURL(),
						 profile.getEmail()
						 );
		startContactImport(provider, user);
		
		startMergingAccountsIfNecessary(domain, user.email().toString(), profile.getEmail());
		
		return user;
	}

	
	private void startMergingAccountsIfNecessary(final Agitter domain, final String email1, final String email2) {
		new Thread() { @Override public void run() {
			try {
				domain.mergeAccountsIfNecessary(email1, email2);
			} catch (UserNotFound e) {
				//OK.
			} catch (Refusal e) {
				e.printStackTrace();
			}
		}}.start();
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
		
		if (candidates.isEmpty()) return;
		new ContactsImport(portalFor(provider), contacts.contactsOf(user), candidates, userProducer)
			.start(); //Optimize: use a thread pool instead of starting tons of threads.

	}


	private boolean providesUsefulContacts(AuthProvider provider) {
		Portal portal = portalFor(provider);
		if (portal == Facebook || portal == Twitter) return false;
		return true;
	}

	
	private Portal portalFor(AuthProvider provider) {
		String id = provider.getProviderId();
		if (id.equals("hotmail")) return WindowsLive;
		if (id.equals("facebook")) return Facebook;
		if (id.equals("google")) return Google;
		if (id.equals("twitter")) return Twitter;
		if (id.equals("yahoo")) return Yahoo;
		throw new IllegalStateException("Unknown OAuth provider: " + id);
	}


	private String idFor(Portal portal) {
		if (portal == WindowsLive) return "hotmail";
		return portal.name().toLowerCase();
	}


	public String getUsername(Profile candidate) {	
		if (candidate.getDisplayName() != null) return candidate.getDisplayName();
		if (candidate.getFullName() != null) return candidate.getFullName();
		
		String name = "";
		if (candidate.getFirstName() != null) name = candidate.getFirstName() + " ";
		if (candidate.getLastName() != null) name = name + candidate.getLastName();
		if (!name.trim().equals("")) return name.trim();
		
		return candidate.getValidatedId();
	}
	
}
