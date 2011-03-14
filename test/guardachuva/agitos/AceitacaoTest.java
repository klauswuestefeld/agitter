package guardachuva.agitos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.server.DateTimeUtilsServer;
import guardachuva.agitos.server.application.ApplicationImpl;
import guardachuva.agitos.shared.Application;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.UserDTO;
import guardachuva.agitos.shared.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.foundation.brickness.testsupport.BrickTestRunner;

@RunWith(BrickTestRunner.class)
public class AceitacaoTest {
	
	{
		my(BrickTestRunner.class).instanceBeingInitialized(this);
	}

	private Application _app;
	private UserDTO _user;
	private SessionToken _session;
	
	@Before
	public void setup() throws ValidationException, UserAlreadyExistsException, UnauthorizedBusinessException {
		_app = new ApplicationImpl();
		_session = _app.createNewUser("admin", "Admin", "password", "admin@email.com");
		_user = _app.getLoggedUserOn(_session);
	}

	@Test
	public void createANewUser() {
		assertNotNull(_user);
		assertEquals("Admin", _user.getUserName());
		assertEquals("admin", _user.getName());
		assertEquals("admin@email.com", _user.getEmail());
	}
	
	@Test
	public void authenticateUser() throws UnauthorizedBusinessException {
		_session = _app.authenticate("admin@email.com", "password");
		assertNotNull(_session);
	}

	@Test(expected=UnauthorizedBusinessException.class)
	public void authenticateUserWithWrongPassword() throws UnauthorizedBusinessException {
		_app.authenticate("admin@email.com", "WrongPassword");
	}

	@Test
	public void createANewEvent() throws Exception {
		_app.createEvent(_session, "Evento", 
				DateTimeUtilsServer.strToDate("13/10/2010 10:45"));

		EventDTO[] events = _app.getEventsForMe(_session);
		assertNotNull(events);
		assertTrue(events.length==1);
		assertEquals("Evento", events[0].getDescription());
		assertEquals(DateTimeUtilsServer.strToDate("13/10/2010 10:45"), events[0].getDate());
	}
		
}
