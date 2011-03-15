package guardachuva.agitos.server.resource;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.ValidationException;

public class UsersResource extends UnauthenticatedBaseResource {

	@Override
	protected Object doPost() {
		try {
			_application.createNewUser(
				getParam("name"), getParam("userName"), 
				getParam("password"), getParam("email"));
			
		} catch (ValidationException e) {
			
			_response.setStatus(SC_BAD_REQUEST);
			return e.getValidationErrors();
			
		} catch (UserAlreadyExistsException e) {
			
			_response.setStatus(SC_BAD_REQUEST);
			return new String[] {e.getMessage()};
			
		}
		
		return true;
	}
	
}
