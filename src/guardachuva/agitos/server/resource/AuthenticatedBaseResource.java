package guardachuva.agitos.server.resource;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;

public class AuthenticatedBaseResource extends UnauthenticatedBaseResource {
	
	protected SessionToken _session;
	
	@Override
	protected void beforeService() {
		super.beforeService();
		try {
		String userName = _request.getCookieValue("userName");
		String password = _request.getCookieValue("password");
		_session = _application.authenticate(userName, password);
		} catch (UnauthorizedBusinessException e) {
			_response.setStatus(SC_UNAUTHORIZED);
		}
	}
	
}
