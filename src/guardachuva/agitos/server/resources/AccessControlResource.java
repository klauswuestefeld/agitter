package guardachuva.agitos.server.resources;

import guardachuva.agitos.shared.UnauthorizedBusinessException;


public class AccessControlResource extends UnauthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		try {
			String userName = _request.getParameter("userName");
			String password = _request.getParameter("password");
			_application.authenticate(userName, password);
			return true;
		} catch (UnauthorizedBusinessException ex) {
			return false;
		}
	}

}
