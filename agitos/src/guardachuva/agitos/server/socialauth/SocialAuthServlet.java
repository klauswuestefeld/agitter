package guardachuva.agitos.server.socialauth;

import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.rpc.RemoteApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.gdevelop.gwt.syncrpc.SyncProxy;

public class SocialAuthServlet extends HttpServlet {

	private HttpServletRequest _request;
	private HttpServletResponse _response;
	private HttpSession _session;
	private RemoteApplication _application;

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
		AuthProvider provider = AuthProviderFactory.getInstance(_request
				.getParameter("id"));
		String redirect = provider
				.getLoginRedirectURL("http://127.0.0.1:8888/agitos/social_auth?status=success");
		_session.setAttribute("AuthProvider", provider);
		redirect(redirect);
	}

	private void redirect(String redirect) {
		_response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		_response.setHeader("Location", redirect);
	}

	private void authenticationSucceded() throws Exception {
		AuthProvider provider = (AuthProvider) _request.getSession()
				.getAttribute("AuthProvider");
		if (provider != null) {
			Profile profile = provider.verifyResponse(_request);
			System.out.println("Autenticado: " + profile);
			System.out.println("Obtendo lista de contatos");
			List<Contact> contactList = provider.getContactList();
			StringBuffer emails = new StringBuffer();
			for (Contact contact : contactList) {
				emails.append("< " + contact.getFirstName() + " > ");
				emails.append(contact.getEmail());
				emails.append(" , ");
			}
			try {
				SessionToken sessionToken = new SessionToken(getSessionTokenFromCookies());
				getApp().addContactsToMe(sessionToken, emails.toString(), "");
			} catch (Exception e) {
				System.out.println(e.getMessage() + " for: " + emails.toString());
			}
			
			redirect("http://127.0.0.1:8888/" + "index.html?" + buildCodeSvrParam() + "#meus_agitos");
		} else {
			throw new ServletException("");
		}
	}

	private String buildCodeSvrParam() {
		final String codesvr = _request.getParameter("gwt.codesvr");
		return (codesvr !=null ? "gwt.codesvr="+codesvr : "");
	}

	private String getSessionTokenFromCookies() throws UnsupportedEncodingException {
		return getCookieValue(SessionToken.COOKIE_NAME);
	}

	private String getCookieValue(final String cookieName) throws UnsupportedEncodingException {
		for (Cookie cookie : _request.getCookies()) {
			if(cookieName.equals(cookie.getName())) {
				return URLDecoder.decode(cookie.getValue(), "UTF-8");
			}
		}
		return "";
	}

	private RemoteApplication getApp() {
		if(_application==null)
			_application = (RemoteApplication) SyncProxy.newProxyInstance(
					RemoteApplication.class, "http://127.0.0.1:8888/agitos/", "rpc");
		return _application;
	}

	private static final long serialVersionUID = 1L;
}
