package guardachuva.agitos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.domain.Event;
import guardachuva.agitos.domain.User;
import guardachuva.agitos.server.application.Application;
import guardachuva.agitos.server.application.DateTimeUtils;
import guardachuva.agitos.server.application.IApplication;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.ValidationException;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.foundation.brickness.testsupport.BrickTestRunner;

@RunWith(BrickTestRunner.class)
public class AceitacaoTest {
	
	{
		my(BrickTestRunner.class).instanceBeingInitialized(this);
	}

	private IApplication app;
	private User user;
	
	@Before
	public void setup() throws ValidationException, UserAlreadyExistsException {
		app = new Application();
		user = app.createUser("admin", "Admin", "password", "admin@email.com");
	}

	@Test
	public void createANewUser() throws ValidationException {

		assertNotNull(user);
		assertEquals("Admin", user.getUserName());
		assertEquals("admin", user.getName());
		assertTrue(user.isValidPassword("password")); //Weird.. had user.getPassword()
		assertEquals("admin@email.com", user.getEmail());
		assertEmpty(user.getContacts());
		assertEmpty(user.getProducers());
	}
	
	@Test
	public void authenticateUser() throws ValidationException, UnauthorizedBusinessException {
		User authenticatedUser = app.authenticate("admin@email.com", "password");
		assertEquals(user, authenticatedUser);
	}

	@Test(expected=UnauthorizedBusinessException.class)
	public void authenticateUserWithWrongPassword() throws ValidationException, UnauthorizedBusinessException {
		app.authenticate("admin@email.com", "WrongPassword");
	}

	@Test
	public void createANewEvent() throws Exception {
		Event event = app.createEvent(user, "Evento", "13/10/2010 10:45");

		assertNotNull(event);
		assertEquals("Evento", event.getDescription());
		assertEquals(DateTimeUtils.strToDate("13/10/2010 10:45"), event.getDate());
	}
	
	private void assertEmpty(Collection<?> collection) {
		assertTrue(collection.isEmpty());
	}


}
