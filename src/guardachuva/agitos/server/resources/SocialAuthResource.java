package guardachuva.agitos.server.resources;

import guardachuva.agitos.domain.User;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.AuthProviderFactory;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;


public class SocialAuthResource extends AuthenticatedBaseResource {
	transient final Log LOG = LogFactory.getLog(SocialAuthResource.class);
	
	@Override
	protected Object doGet() throws Exception {
		
		LOG.info(_request.getRequest());
		
		/*
		LogFactory.getLog(GoogleImpl.class);		
		java.util.logging.Logger.getLogger("org.brickred.socialauth.provider.GoogleImpl").setLevel(Level.ALL);
		/**/
		
		if("success".equals(_request.getParameter("status"))) {
			authenticationSucceded();
		} else {
			authenticationRequested();
		}
		return "";
	}

	private void authenticationRequested() throws Exception {
		AuthProvider provider = AuthProviderFactory.getInstance(_request.getParameter("id"));
		String redirect = provider.getLoginRedirectURL("http://127.0.0.1:8888/api/social_auth?status=success");
		redirect(redirect);
		_request.getSession().setAttribute("AuthProvider", provider);
	}

	private void redirect(String redirect) {
		_response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		_response.setHeader("Location", redirect);
	}

	private void authenticationSucceded() throws Exception {
		AuthProvider provider = (AuthProvider) _request.getSession().getAttribute("AuthProvider");
		if(provider != null) {
			Profile profile = provider.verifyResponse(_request.getRequest());
			LOG.info("Autenticado: " + profile);
			LOG.debug("Obtendo lista de contatos");
			List<Contact> contactList = provider.getContactList();
			for (Contact contact : contactList) {
				LOG.debug(contact);
				try {
				User newUser = _application.getUserHome().produceUser(contact.getEmail());
				_user.addContact(newUser);
				} catch (Exception e) {
					LOG.warn(e.getMessage() +  " for: " + contact.toString());
				}
			}
			redirect("http://127.0.0.1:8888/index.html?gwt.codesvr=127.0.0.1:9997#meus_agitos");
		}
	}

	
}
