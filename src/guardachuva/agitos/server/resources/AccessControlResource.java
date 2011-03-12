package guardachuva.agitos.server.resources;

import guardachuva.agitos.shared.UnauthorizedBusinessException;


public class AccessControlResource extends UnauthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		try {
			_application.getUserHome().authenticate(_request.getParameter("userName"), _request.getParameter("password"));
			return true;
		} catch (UnauthorizedBusinessException ex) {
			return false;
		}
	}

}
