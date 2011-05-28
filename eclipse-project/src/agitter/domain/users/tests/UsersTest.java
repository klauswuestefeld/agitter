package agitter.domain.users.tests;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.domain.users.Users.InvalidPassword;
import agitter.domain.users.Users.UserNotFound;
import agitter.domain.users.UsersImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;

public class UsersTest extends CleanTestBase {

	private final Users _subject = new UsersImpl();

	@BeforeClass
	static public void setConsoleLogLevel() {
		Logger.getLogger("").setLevel(Level.WARNING);
	}
	
	@Test
	public void signup() throws Refusal {
		assertSignUp("ana", "ana@gmail.com", "ana123");
		assertSignUp("bruninho", "bruno@gmail.com", "brunox");
	}
	
	@Test
	public void loginWithUsername() throws Refusal {
		signUpAna();
		User user = _subject.loginWithUsername("ana", "ana123");
		assertAna(user);
	}
	
	@Test(expected = UserNotFound.class)
	public void loginWithUnknownUserName() throws Refusal {
		_subject.loginWithUsername("unknown_username", "irrelevant");
	}

	@Test(expected = InvalidPassword.class)
	public void loginWithInvalidPassword() throws Refusal {
		signUpAna();
		_subject.loginWithUsername("ana", "ana000");
	}
	
	@Test
	public void loginWithEmail() throws Refusal {
		signUpAna();
		User user = _subject.loginWithEmail("ana@gmail.com", "ana123");
		assertAna(user);
	}
	
	@Test(expected = UserNotFound.class)
	public void loginWithUnknownEmail() throws Refusal {
		_subject.loginWithEmail("unknown_email@somewhere.com", "irrelevant");
	}
	
	@Test
	public void findUserByUsername() throws Refusal{
		signUpAna();
		User user = _subject.findByUsername("ana");
		assertEquals("ana@gmail.com", user.email());
	}
	
	@Test(expected = UserNotFound.class)
	public void findUserByUsernameWithNoResults() throws Refusal {
		_subject.findByUsername("unknown");
	}
	
	@Test
	public void findUserByEmail() throws Refusal{
		signUpAna();
		User user = _subject.findByEmail("ana@gmail.com");
		assertEquals("ana", user.username());
	}
	
	@Test(expected = UserNotFound.class)
	public void findUserByEmailWithNoResults() throws Refusal {
		_subject.findByEmail("unknown@somewhere.com");
	}
	
	@Test(expected = Refusal.class)
	public void signupWithBlankUsername() throws Refusal{
		_subject.signup( "", "myself@email.com", "$ecret" );
	}

	@Test(expected = Refusal.class)
	public void signupWithBlankEmail() throws Refusal{
		_subject.signup( "myself", "", "$ecret" );
	}

	@Test(expected = Refusal.class)
	public void signupWithBlankPassword() throws Refusal{
		_subject.signup( "myself", "myself@email.com", "" );
	}

	@Test(expected = Refusal.class)
	public void signupWithBlankUsernameEmailAndPassword() throws Refusal{
		_subject.signup( "", "", "" );
	}

	@Test
	public void signupTryingToCheat() throws Refusal{
		final String[][] cenarios = {
			{ null, "myself@email.com", "12345" },
			{ " ", "myself@email.com", "12345" },
			{ "		", "myself@email.com", "12345" },
			{ "user", null, "12345" },
			{ "user", " ", "12345" },
			{ "user", "		", "12345" },
			{ "user", "myself@email.com", null },
			{ "user", "myself@email.com", " " },
			{ "user", "myself@email.com", "		" },
		};
		for(final String[] cenario : cenarios) {
			try {
				_subject.signup( cenario[ 0 ], cenario[ 1 ], cenario[ 2 ] );
				Assert.fail( "Expected Refusal trying to signup with: " + Arrays.toString( cenario ) );
			}catch(final Refusal refusal) {
				//expected
			}
		}
	}

	private void assertAna(User user) {
		assertEquals("ana", user.username());
		assertEquals("ana@gmail.com", user.email());
	}
	
	
	
	private void signUpAna() throws Refusal{
		signUp("ana", "ana@gmail.com", "ana123");
	}
	
	private void assertSignUp(String username, String email, String password) throws Refusal {
		User user = signUp(username, email, password);
		assertEquals(username, user.username());
	}

	private User signUp(String username, String email, String password)
			throws Refusal {
		User user = _subject.signup(username, email, password);
		return user;
	}

}
