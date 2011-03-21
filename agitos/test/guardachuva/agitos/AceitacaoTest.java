package guardachuva.agitos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import guardachuva.agitos.server.ApplicationImpl;
import guardachuva.agitos.server.DateTimeUtilsServer;
import guardachuva.agitos.shared.Application;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.UserDTO;
import guardachuva.agitos.shared.ValidationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AceitacaoTest {
	
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
	@Ignore
	public void createANewEvent() throws Exception {
		// FIXME: Deve setear a data atual...
		_app.createEvent(_session, "Evento", 
				DateTimeUtilsServer.strToDate("13/10/2010 10:45"));

		EventDTO[] events = _app.getEventsForMe(_session);
		assertNotNull(events);
		assertTrue(events.length==1);
		assertEquals("Evento", events[0].getDescription());
		assertEquals(DateTimeUtilsServer.strToDate("13/10/2010 10:45"), events[0].getDate());
	}
	
	@Test
	public void addContactsInLargeBathes() throws ValidationException, Exception {
		_app.addContactsToMe(_session, "\"Alisson Vale\" <alissoncvale@gmail.com>,bihaiko@gmail.com , leonardonunes@gmail.com, matias.g.rodriguez@gmail.com , , ", "");
		UserDTO[] contacts = _app.getContactsForMe(_session);
		assertThat(contacts.length, equalTo(4));
	}
		
}
