package agitter.server.socialauth;

import static agitter.server.utils.Flash.flash;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.AuthProviderFactory;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;

import agitter.server.domain.ApplicationImpl;
import agitter.shared.SessionToken;
import agitter.shared.UnauthorizedBusinessException;
import agitter.shared.UserDTO;
import agitter.shared.ValidationException;

public class SocialAuthServlet extends HttpServlet {

	private static final String AUTH_PROVIDER = "AuthProvider";
	private static final String OAUTH_CONSUMER_PROPERTIES = "oauth_consumer.properties";
	private HttpServletRequest _request;
	private HttpServletResponse _response;
	private HttpSession _session;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		setup(request, response);

		try {
			if (hasAuthenticationSucceded()) {
				processAuthenticationSuccess();
			} else {
				processAuthenticationRequest();
			}
		} catch (Exception e) {
			flash(_request).error(e.getMessage());
			redirectToAgitos();
		}
	}

	private void processAuthenticationRequest() throws Exception {
		AuthProvider provider;
		final String providerName = _request.getParameter("id");

		try {
			provider = getAuthProviderFor(providerName);
			_session.setAttribute(AUTH_PROVIDER, provider);
		} catch (Exception e) {
			throw new RuntimeException(
					"Problema tentando autenticar-se usando " + providerName, e);
		}

		redirect(provider.getLoginRedirectURL(_request.getRequestURL()
				.toString() + "?status=success"));
	}

	private void processAuthenticationSuccess() throws Exception {
		AuthProvider provider;
		provider = getAuthProviderFromSession();
		Profile profile = provider.verifyResponse(_request);
		System.out.println("Autenticado: " + profile);
		importContacts(provider.getContactList(), provider.getClass()
				.toString());
		redirectToAgitos();
	}

	protected void importContacts(final List<Contact> contactList,
			final String providerName) throws ValidationException,
			UnsupportedEncodingException, UnauthorizedBusinessException {
		List<UserDTO> contactsToImport = new ArrayList<UserDTO>();

		for (Contact contact : contactList) {
			contactsToImport.add(new UserDTO(null, null, contact.getEmail()));
		}

		SessionToken sessionToken = new SessionToken(
				getSessionTokenFromCookies());

		ApplicationImpl.GetInstance().importContactsFromService(sessionToken,
				contactsToImport, providerName);
	}

	protected AuthProvider getAuthProviderFor(final String providerName)
			throws Exception {
		return AuthProviderFactory.getInstance(providerName,
				pathToPropertyFile());
	}

	protected String pathToPropertyFile() {
		return getClass().getPackage().getName().replace('.', '/') + "/"
				+ OAUTH_CONSUMER_PROPERTIES;
	}

	private AuthProvider getAuthProviderFromSession() {
		AuthProvider provider = (AuthProvider) _request.getSession()
				.getAttribute(AUTH_PROVIDER);
		if (provider == null)
			throw new RuntimeException(
					"A autenticação não pode ser realizada, já que informações importantes não se encontram na sessão do usuario.");
		return provider;
	}

	private void redirectToAgitos() {
		redirect("/index.html?" + buildCodeSvrParam() + "#meus_agitos");
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

	private void redirect(String redirect) {
		_response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		_response.setHeader("Location", redirect);
	}

	private void setup(HttpServletRequest request, HttpServletResponse response) {
		_request = request;
		_response = response;
		_session = request.getSession();
	}

	private boolean hasAuthenticationSucceded() {
		return "success".equals(_request.getParameter("status"));
	}

	private static final long serialVersionUID = 1L;
}
