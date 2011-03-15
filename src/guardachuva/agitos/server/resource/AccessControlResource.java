package guardachuva.agitos.server.resource;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;

public class AccessControlResource extends UnauthenticatedBaseResource {

	@Override
	protected Object doGet() {
		try {
			String userName = _request.getParameter("userName");
			String password = _request.getParameter("password");
			SessionToken session = _application.authenticate(userName, password);
			return session.getToken();
		} catch (UnauthorizedBusinessException ex) {
			this._response.setStatus(SC_UNAUTHORIZED);
			return null;
		}
	}

}
