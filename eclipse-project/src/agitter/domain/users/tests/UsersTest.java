package agitter.domain.users.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.domain.users.Users.InvalidPassword;
import agitter.domain.users.Users.UserNotFound;
import agitter.domain.users.UsersImpl;

public class UsersTest extends CleanTestBase {

	private final Users _subject = new UsersImpl();

	
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
