package spike;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;

public class CallBackServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AuthProvider provider = (AuthProvider) req.getSession().getAttribute("provider");
		Profile profile;
		try {
			System.out.println("Provider: " + provider);
			if (provider == null) return;
			profile = provider.verifyResponse(req);
			resp.getOutputStream().println("<html>");
			resp.getOutputStream().println("Signign in...");
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("ValidatedID: " + profile.getValidatedId());
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("FullName: " + profile.getFullName());
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("Image:");
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("<img src=\"" + profile.getProfileImageURL() + "\" />");
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("Email: " + profile.getEmail());
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("Contacts:");
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("<ul>");
			for (Contact c: provider.getContactList()) {
				resp.getOutputStream().println("<li>" + c.getDisplayName() + ", " + c.getEmail() + "</li>");
			}
			resp.getOutputStream().println("</ul>");
			resp.getOutputStream().println("</html>");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
