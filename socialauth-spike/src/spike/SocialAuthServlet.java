package spike;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;

public class SocialAuthServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String providerId = req.getParameter("providerid");
		resp.getOutputStream().print("Provider: " + providerId);
		
		//Create an instance of SocialAuthConfgi object
		SocialAuthConfig config = SocialAuthConfig.getDefault();
		
		try {

			//load configuration. By default load the configuration from oauth_consumer.properties. 
			//You can also pass input stream, properties object or properties file name.
			config.load();
			
			//Create an instance of SocialAuthManager and set config
			SocialAuthManager manager = new SocialAuthManager();
			manager.setSocialAuthConfig(config);
			
			//URL of YOUR application which will be called after authentication
			String successUrl = "http://localhost/authresp";
			
			// get Provider URL to which you should redirect for authentication.
			// id can have values "facebook", "twitter", "yahoo" etc. or the OpenID URL
			String url = manager.getAuthenticationUrl(providerId, successUrl);
			
			// Store in session
			req.getSession().setAttribute("authManager", manager);
	
			resp.sendRedirect(url);

		} catch (Exception e) {
			e.printStackTrace();
		}

		
//		String providerId = req.getParameter("providerId");
//		resp.getOutputStream().print("Provider: " + providerId);
//		AuthProvider provider;
//		try {
//		
//			provider = AuthProviderFactory.getInstance(providerId);
//			// String url = provider.getLoginRedirectURL("http://agitter.com/authresp");
//			String url = provider.getLoginRedirectURL("http://localhost/authresp");
//			req.getSession().setAttribute("provider", provider);
//			
//			resp.sendRedirect(url);
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}
	
}
