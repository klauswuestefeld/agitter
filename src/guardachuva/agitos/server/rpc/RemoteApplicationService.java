package guardachuva.agitos.server.rpc;

import guardachuva.agitos.shared.Application;
import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.UserDTO;
import guardachuva.agitos.shared.ValidationException;
import guardachuva.agitos.shared.rpc.RemoteApplication;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RemoteApplicationService extends RemoteServiceServlet implements
		RemoteService, RemoteApplication {

	private static final long serialVersionUID = 1L;
	
	private Application app;

	public RemoteApplicationService(Application application) {
		app = application;
	}
	
	@Override
	public SessionToken createNewUser(String name, String userName, String password, String email) throws ValidationException, UserAlreadyExistsException {
		return app.createNewUser(name, userName, password, email);
	}

	@Override
	public SessionToken authenticate(String email, String password) throws UnauthorizedBusinessException {
		return app.authenticate(email, password);
	}

	@Override
	public EventDTO[] getEventsForMe(SessionToken session) throws UnauthorizedBusinessException {
		return app.getEventsForMe(session);
	}

	@Override
	public void createEvent(SessionToken session, String description, Date date) throws BusinessException {
		app.createEvent(session, description, date);
	}

	@Override
	public void removeEventForMe(SessionToken session, int id) throws BusinessException {
		app.removeEventForMe(session, id);
	}

	@Override
	public void addContactsToMe(SessionToken session, String contact_mail, String linkToApplication) throws ValidationException, Exception {
		app.addContactsToMe(session, contact_mail, linkToApplication);
	}

	@Override
	public void deleteContactForMe(SessionToken session, String email) throws BusinessException {
		app.deleteContactForMe(session, email);
	}

	@Override
	public UserDTO[] getContactsForMe(SessionToken session) throws UnauthorizedBusinessException {
		return app.getContactsForMe(session);
	}

	@Override
	public void ignoreProducerForMe(SessionToken session, String email) throws ValidationException, BusinessException {
		app.ignoreProducerForMe(session, email);
	}

	@Override
	public void logout(SessionToken session) throws UnauthorizedBusinessException {
		app.logout(session);
	}

	@Override
	public UserDTO getLoggedUserOn(SessionToken session)
			throws UnauthorizedBusinessException {
		return app.getLoggedUserOn(session);
	}

}
