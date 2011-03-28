package guardachuva.agitos.server.domain.tests;

import guardachuva.agitos.server.domain.Event;
import guardachuva.agitos.server.domain.Events;
import guardachuva.agitos.server.domain.User;
import guardachuva.agitos.server.domain.Users;
import guardachuva.agitos.server.utils.DateTimeUtilsServer;
import guardachuva.agitos.shared.BusinessException;

import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class UserTest extends Assert {

	private Users _userHome = new Users();
	private Events _eventHome = new Events();
	
	private User _altieres;
	private User _maoki;
	
	public UserTest() throws BusinessException {
		_altieres = _userHome.produceUser("altiereslopes@gmail.com");
		_maoki = _userHome.produceUser("maoki@gmail.com");
	}

	@Test
	public void produceUser() throws BusinessException {
		assertTrue(_userHome.isKnownUser("altiereslopes@gmail.com"));
		assertFalse(_userHome.isKnownUser("joao@gmail.com"));
		
		assertSame(_altieres, _userHome.produceUser("altiereslopes@gmail.com"));
		assertNotNull(_userHome.produceUser("joao@gmail.com"));
	}
	
	@Test
	public void produceCompleteUser() throws BusinessException {
		User _newUser = _userHome.produceUser("Matheus Haddad", "mhaddad", "1234#5", "matheus@webgoal.com.br");
		assertSame(_newUser, _userHome.produceUser("matheus@webgoal.com.br"));
	}
	
	@Test
	public void produceUserWithName() throws BusinessException {
		String userString = "\"Altieres Lopes\" <altiereslopes@gmail.com>";
		User userFromString = _userHome.produceUser(userString);
		assertEquals(_altieres, userFromString);
		
		String[] wrongUserStrings = new String[] {
				"Mauricio Matsuda\" <mauriciomatsuda@gmail.com>",
				"\"Mauricio Matsuda <mauriciomatsuda@gmail.com>",
				"\"Mauricio Matsuda mauriciomatsuda@gmail.com>"
			};
		
		for (String wrongUserString : wrongUserStrings) {
			try {
				_userHome.produceUser(wrongUserString);
				fail();
			} catch (BusinessException ex) {	}
		}
	}
	
	@Test
	public void produceMultipleUsers() throws BusinessException {
		String usersString = "\"Altieres Lopes\" <altiereslopes@gmail.com>, " +
				"\"Mauricio Matsuda\" <mauriciomatsuda@gmail.com>";
		List<User> users = _userHome.produceMultipleUsers(usersString);
		
		assertEquals(_altieres, users.get(0));
		assertNotNull(users.get(1));
	}
	
	@Test
	public void myEvents() throws BusinessException, ParseException {
		Event aniversario = _eventHome.createFor(_altieres, "Aniversário de 1 ano!", DateTimeUtilsServer.strToDate("25/12/1982 12:00"));
		assertEquals(aniversario, _altieres.myEvents().get(0));
	}
	
	@Test
	public void listEvents() throws ParseException, BusinessException {
		Event aniversario = _eventHome.createFor(_altieres, "Aniversário de 1 ano!", DateTimeUtilsServer.strToDate("25/12/1982 12:00"));
		_altieres.addContact(_maoki);
		assertEquals(aniversario, _maoki.listEvents().get(0));
		assertEquals(aniversario, _altieres.listEvents().get(0));
	}
	
	@Test
	public void getContacts() throws BusinessException {
		_altieres.addContact(_maoki);
		assertTrue(_altieres.getContacts().contains(_maoki));
	}
	
	@Test
	public void addExistentContact() throws BusinessException {
		_altieres.addContact(_maoki);
		_altieres.addContact(_maoki);
		assertEquals(1, _altieres.getContacts().size());
		assertEquals(1, _maoki.getProducers().size());
		
		try {
			_altieres.addContact(_altieres);
			fail();
		} catch (BusinessException ex) {	}
	}
	
	@Test
	public void removeContact() throws BusinessException {
		_altieres.addContact(_maoki);
		_altieres.removeContact(_maoki);
		
		assertEquals(0, _altieres.getContacts().size());
		assertEquals(0, _maoki.getProducers().size());
	}
	
	@Test
	public void shouldIgnoreProducer() throws Exception {
		_maoki.addContact(_altieres);
		assertTrue(_altieres.getProducers().contains(_maoki));
		_altieres.ignoreProducer(_maoki);
		assertFalse(_altieres.getProducers().contains(_maoki));
	}
	
	@Test(expected=BusinessException.class)
	public void shouldNotIgnoreHimself() throws BusinessException {
		_altieres.ignoreProducer(_altieres);
	}
	
	@Test
	public void userShouldBeConsideredRegistered() throws BusinessException {
		User john = _userHome.produceUser("john@mailer.com");
		assertFalse(john.isRegistered());
		
		john.registerMe("John Ferri", "johnf", "passw");
		assertTrue(john.isRegistered());
	}
}
