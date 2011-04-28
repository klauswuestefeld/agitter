package spike;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.AuthProviderFactory;

public class SocialAuthServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String providerId = req.getParameter("providerId");
		resp.getOutputStream().print("Provider: " + providerId);
		AuthProvider provider;
		try {
		
			provider = AuthProviderFactory.getInstance(providerId);
			String url = provider.getLoginRedirectURL("http://agitter.com:8888/authresp");   //  "http://localhost/auth"
			req.getSession().setAttribute("provider", provider);
			
			resp.sendRedirect(url);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
