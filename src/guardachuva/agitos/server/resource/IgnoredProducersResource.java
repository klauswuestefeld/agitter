package guardachuva.agitos.server.resource;

import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;

public class IgnoredProducersResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() {
		_response.setStatus(SC_METHOD_NOT_ALLOWED);
		return null;
	}

	@Override
	protected Object doPost() throws Exception {
		String email = _request.getParameter("email");
		_application.ignoreProducerForMe(_session, email);
		return null;
	}
	
	@Override
	protected void doDelete() {
		_response.setStatus(SC_METHOD_NOT_ALLOWED);
	}

}
