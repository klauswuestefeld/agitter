package agitter.controller.oauth;

import infra.logging.LogInfra;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpSession;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;

import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.domain.users.Users;

public class OAuth {

	private final Users users;

	public OAuth(Users users) {
		this.users = users;
	}

	public String googleSigninURL(URL context, HttpSession httpSession) throws Exception {
		String providerId = "google";
		return signinURL(context, httpSession, providerId);
	}
	
	public String windowsSigninURL(URL context, HttpSession httpSession) throws Exception {
		String providerId = "hotmail";
		return signinURL(context, httpSession, providerId);
	}

	public String yahooSigninURL(URL context, HttpSession httpSession) throws Exception {
		String providerId = "yahoo";
		return signinURL(context, httpSession, providerId);
	}
	
	public String facebookSigninURL(URL context, HttpSession httpSession) throws Exception {
		String providerId = "facebook";
		return signinURL(context, httpSession, providerId);
	}
	
	public String twitterSigninURL(URL context, HttpSession httpSession) throws Exception {
		String providerId = "twitter";
		return signinURL(context, httpSession, providerId);
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
		for (String name : params.keySet()) {
			paramsMap.put(name, params.get(name)[0]);
		}
		
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
	
		return UserUtils.produce(users, EmailAddress.email(profile.getEmail()));
	}

}
