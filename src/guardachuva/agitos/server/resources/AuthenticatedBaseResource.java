package guardachuva.agitos.server.resources;

import guardachuva.agitos.domain.User;
import guardachuva.agitos.shared.UnauthorizedBusinessException;

public class AuthenticatedBaseResource extends UnauthenticatedBaseResource {
	
	protected User _user = null;
	
	@Override
	protected void beforeService() throws UnauthorizedBusinessException {
		super.beforeService();
		String userName = _request.getCookieValue("userName");
		String password = _request.getCookieValue("password");
		_user = _application.authenticate(userName, password);
	}
	
}
