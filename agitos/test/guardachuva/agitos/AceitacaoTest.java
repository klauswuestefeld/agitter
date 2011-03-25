package guardachuva.agitos;

import static org.hamcrest.CoreMatchers.equalTo;
import guardachuva.agitos.server.ApplicationImpl;
import guardachuva.agitos.server.DateTimeUtilsServer;
import guardachuva.agitos.shared.Application;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.UserDTO;
import guardachuva.agitos.shared.ValidationException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import sneer.foundation.testsupport.CleanTestBase;

public class AceitacaoTest extends CleanTestBase {
	
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
	public void createNewUserShouldRegisterAlreadyExistentContact() throws ValidationException, Exception{
		String mailOfTheContact = "derek@gmail.com";
		_app.addContactsToMe(_session, mailOfTheContact);
		String password = "drk2011";
		_app.createNewUser("Derek Muller", "derek", password, mailOfTheContact);
		assertNotNull(_app.authenticate(mailOfTheContact, password));
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

		List<EventDTO> events = _app.getEventsForMe(_session);
		assertNotNull(events);
		assertTrue(events.size()==1);
		assertEquals("Evento", events.get(0).getDescription());
		assertEquals(DateTimeUtilsServer.strToDate("13/10/2010 10:45"), events.get(0).getDate());
	}
	
	@Test
	public void addContactsInLargeBatches() throws ValidationException, Exception {
		_app.addContactsToMe(_session, "\"Alisson Vale\" <alissoncvale@gmail.com>,bihaiko@gmail.com , leonardonunes@gmail.com, matias.g.rodriguez@gmail.com , , ");
		List<UserDTO> contacts = _app.getContactsForMe(_session);
		assertThat(contacts.size(), equalTo(4));
	}
		
	@Test
	public void importContactsFromService() throws UnauthorizedBusinessException, ValidationException {
		List<UserDTO> contactsToImport = new ArrayList<UserDTO>();
		contactsToImport.add(new UserDTO("Juan", "juan.bernabo@teamware.com.br", "juan.bernabo@teamware.com.br"));
		_app.importContactsFromService(_session, contactsToImport, "gmail");
		assertThat(contactsToImport, equalTo(_app.getContactsForMe(_session)));
	}
	
}
