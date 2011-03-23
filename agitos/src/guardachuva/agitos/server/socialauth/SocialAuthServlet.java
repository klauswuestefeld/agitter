package guardachuva.agitos.server.socialauth;

import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UserDTO;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.AuthProviderFactory;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;

public class SocialAuthServlet extends ApplicationAwareServlet {

	private static final String OAUTH_CONSUMER_PROPERTIES = "oauth_consumer.properties";
	private HttpServletRequest _request;
	private HttpServletResponse _response;
	private HttpSession _session;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		_request = request;
		_response = response;
		_session = request.getSession();

		try {
			if ("success".equals(request.getParameter("status"))) {
				authenticationSucceded();
			} else {
				authenticationRequested();
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void authenticationRequested() throws Exception {
		System.out.println("Using SocialAuth properties from: " + pathToPropertyFile());

		AuthProvider provider = AuthProviderFactory.getInstance(
				_request.getParameter("id"), pathToPropertyFile());

		String redirect = provider.getLoginRedirectURL(AGITOS_URL
				+ "/agitosweb/social_auth?status=success");
		_session.setAttribute("AuthProvider", provider);
		redirect(redirect);
	}

	private String pathToPropertyFile() {
		return getClass().getPackage().getName().replace('.', '/') + "/" + OAUTH_CONSUMER_PROPERTIES; 
	}

	private void redirect(String redirect) {
		_response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		_response.setHeader("Location", redirect);
	}

	private void authenticationSucceded() throws Exception {
		try {
			AuthProvider provider = (AuthProvider) _request.getSession()
					.getAttribute("AuthProvider");
			if (provider == null)
				throw new ServletException("AuthProvider not found in session");

			Profile profile = provider.verifyResponse(_request);
			System.out.println("Autenticado: " + profile);

			List<UserDTO> contactsToImport = new ArrayList<UserDTO>();
			
			for (Contact contact : provider.getContactList()) {
				contactsToImport
						.add(new UserDTO(null, null, contact.getEmail()));
			}

			SessionToken sessionToken = new SessionToken(
					getSessionTokenFromCookies());
			
			getApp().importContactsFromService(sessionToken, contactsToImport,
					provider.getClass().toString());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		redirect(AGITOS_URL + "/index.html?" + buildCodeSvrParam()
				+ "#meus_agitos");
	}

	private String buildCodeSvrParam() {
		final String codesvr = _request.getParameter("gwt.codesvr");
		return (codesvr != null ? "gwt.codesvr=" + codesvr : "");
	}

	private String getSessionTokenFromCookies()
			throws UnsupportedEncodingException {
		return getCookieValue(SessionToken.COOKIE_NAME);
	}

	private String getCookieValue(final String cookieName)
			throws UnsupportedEncodingException {
		for (Cookie cookie : _request.getCookies()) {
			if (cookieName.equals(cookie.getName())) {
				return URLDecoder.decode(cookie.getValue(), "UTF-8");
			}
		}
		return "";
	}

	private static final long serialVersionUID = 1L;
}
