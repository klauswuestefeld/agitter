package guardachuva.agitos.server.resources;

import guardachuva.agitos.domain.User;

import org.eclipse.jetty.server.Response;


public class UsersResource extends UnauthenticatedBaseResource {

	@Override
	protected Object doPost() throws Exception {
		if (_application.getUserHome().isKnownUser(_request.getParameter("email"))) {
			_response.setStatus(Response.SC_BAD_REQUEST);
			return new String[] {"Usuário já existente."};			
		}
		
		String[] errors = User.errorsForConstruction(
				_request.getParameter("name"),
				_request.getParameter("userName"),
				_request.getParameter("password"),
				_request.getParameter("email"));
		
		if (errors.length > 0) {
			_response.setStatus(Response.SC_BAD_REQUEST);
			return errors;
		}
		
		_application.getUserHome().produceUser(
				_request.getParameter("name"),
				_request.getParameter("userName"),
				_request.getParameter("password"),
				_request.getParameter("email"));
		
		return true;
	}
	
}
