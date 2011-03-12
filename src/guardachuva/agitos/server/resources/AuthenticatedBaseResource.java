package guardachuva.agitos.server.resources;

import guardachuva.agitos.domain.User;
import guardachuva.agitos.shared.UnauthorizedBusinessException;

public class AuthenticatedBaseResource extends UnauthenticatedBaseResource {
	
	protected User _user = null;
	
	@Override
	protected void beforeService() throws UnauthorizedBusinessException {
		super.beforeService();
		_user = _application.getUserHome().authenticate(_request.getCookieValue("userName"), _request.getCookieValue("password"));
	}
	
}
