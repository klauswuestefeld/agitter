package guardachuva.agitos.server.resource;

import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;

import org.eclipse.jetty.server.Response;

public class AuthenticatedBaseResource extends UnauthenticatedBaseResource {
	
	protected SessionToken _session;
	
	@Override
	protected void beforeService() throws UnauthorizedBusinessException {
		super.beforeService();
		try {
		String userName = _request.getCookieValue("userName");
		String password = _request.getCookieValue("password");
		_session = _application.authenticate(userName, password);
		} catch (UnauthorizedBusinessException e) {
			_response.setStatus(Response.SC_UNAUTHORIZED);
			throw e;
		}
	}
	
}
