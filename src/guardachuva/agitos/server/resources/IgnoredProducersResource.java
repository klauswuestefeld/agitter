package guardachuva.agitos.server.resources;

import org.eclipse.jetty.server.Response;

public class IgnoredProducersResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		_response.setStatus(Response.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	@Override
	protected Object doPost() throws Exception {
		String email = _request.getParameter("email");
		_application.ignoreProducerFor(_user, email);
		return null;
	}
	
	@Override
	protected void doDelete() throws Exception {
		_response.setStatus(Response.SC_METHOD_NOT_ALLOWED);
	}

}
