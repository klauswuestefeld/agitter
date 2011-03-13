package guardachuva.agitos.server.resource;

import org.eclipse.jetty.server.Response;

import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;


public class AccessControlResource extends UnauthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		try {
			String userName = _request.getParameter("userName");
			String password = _request.getParameter("password");
			SessionToken session = _application.authenticate(userName, password);
			return session.getToken();
		} catch (UnauthorizedBusinessException ex) {
			this._response.setStatus(Response.SC_UNAUTHORIZED);
			return null;
		}
	}

}
