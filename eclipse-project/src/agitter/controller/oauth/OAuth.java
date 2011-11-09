package agitter.controller.oauth;

import infra.logging.LogInfra;

import java.net.URL;
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

import sneer.foundation.lang.Functor;
import agitter.controller.oauth.contactsimport.ContactsImport;
import agitter.domain.contacts.Contacts;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

public class OAuth {

	private final Functor<EmailAddress, User> userProducer;
	private final Contacts contacts;

	
	public OAuth(Functor<EmailAddress, User> userProducer, Contacts contacts) {
		this.userProducer = userProducer;
		this.contacts = contacts;
	}

	
	public String googleSigninURL(URL context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, "google");
	}
	public String windowsSigninURL(URL context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, "hotmail");
	}
	public String yahooSigninURL(URL context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, "yahoo");
	}
	public String facebookSigninURL(URL context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, "facebook");
	}
	public String twitterSigninURL(URL context, HttpSession httpSession) throws Exception {
		return signinURL(context, httpSession, "twitter");
	}
	
	
	private String signinURL(URL context, HttpSession httpSession, String providerId) throws Exception {
		SocialAuthConfig config = SocialAuthConfig.getDefault();
		try {
			config.load();
	
			SocialAuthManager manager = new SocialAuthManager();
			manager.setSocialAuthConfig(config);
			
			String callbackUrl = context.toString() + "oauth";
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
			System.out.println("Provider: " + provider);
			System.out.println("ProviderClass: " + provider.getClass());
	
			profile = provider.getUserProfile();
		} catch (Exception e) {
			LogInfra.getLogger(this).log(Level.SEVERE, "Erro no retorno do SocialAuth. Provider: " + provider + "\n" + e.getMessage());
			throw e;
		}
	
		User user = userProducer.evaluate(EmailAddress.email(profile.getEmail()));
		startContactImport(provider, user);
		return user;
	}


	private void startContactImport(AuthProvider provider, User user) {
		List<Contact> candidates;
		try {
			candidates = provider.getContactList();
		} catch (Exception e) {
			LogInfra.getLogger(this).severe("Erro importando contatos via SocialAuth. Provider: " + provider + "\n" + e.getMessage());
			return;
		}
		
		if (candidates.isEmpty()) return;
		new ContactsImport(contacts.contactsOf(user), candidates, userProducer)
			.start(); //Optimize: use a thread pool instead of starting tons of threads.
	}

}
