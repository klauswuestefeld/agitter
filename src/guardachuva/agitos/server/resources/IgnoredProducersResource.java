package guardachuva.agitos.server.resources;

import guardachuva.agitos.domain.User;

import org.eclipse.jetty.server.Response;

public class IgnoredProducersResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		_response.setStatus(Response.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	@Override
	protected Object doPost() throws Exception {
		String contact_mail = _request.getParameter("email");
		User producer = _application.getUserHome().produceUser(contact_mail);
		_user.ignoreProducer(producer);
		return null;
	}
	
	@Override
	protected void doDelete() throws Exception {
		_response.setStatus(Response.SC_METHOD_NOT_ALLOWED);
	}

}
