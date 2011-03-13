package guardachuva.agitos.server.resources;

import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.ValidationException;

import org.eclipse.jetty.server.Response;


public class UsersResource extends UnauthenticatedBaseResource {

	@Override
	protected Object doPost() throws Exception {
		try {
			_application.createUser(
				getParam("name"), getParam("userName"), 
				getParam("password"), getParam("email"));
			
		} catch (ValidationException e) {
			
			_response.setStatus(Response.SC_BAD_REQUEST);
			return e.getValidationErrors();
			
		} catch (UserAlreadyExistsException e) {
			
			_response.setStatus(Response.SC_BAD_REQUEST);
			return new String[] {e.getMessage()};
			
		}
		
		return true;
	}
	
}
