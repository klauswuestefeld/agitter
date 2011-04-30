package spike;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.provider.TwitterImpl;

public class CallBackServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AuthProvider provider = (AuthProvider) req.getSession().getAttribute("provider");

		System.out.println("Provider: " + provider);
		if (provider == null) return;
		System.out.println("ProviderClass: " + provider.getClass());
		
		Profile profile;
		try {
			profile = provider.verifyResponse(req);
			resp.getOutputStream().println("<html>");
			resp.getOutputStream().println("Signign in...");
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("ValidatedID: " + profile.getValidatedId());
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("First Name: " + profile.getFirstName());
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("Last Name: " + profile.getLastName());
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("Full Name: " + profile.getFullName());
			resp.getOutputStream().println("<br />");
			resp.getOutputStream().println("Display Name: " + profile.getDisplayName());
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
			for (Contact c: contactList(provider)) {
				resp.getOutputStream().println("<li>" + c.getDisplayName() + ", " + c.getEmail() + "</li>");
			}
			resp.getOutputStream().println("</ul>");
			resp.getOutputStream().println("</html>");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<Contact> contactList(AuthProvider provider) throws Exception {
		if (provider instanceof TwitterImpl) {
			return ((TwitterImpl)provider).getContactList();
		}
		return provider.getContactList();
	}
	
}
