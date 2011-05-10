package agitter.domain.users.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.domain.users.UsersImpl;

public class UsersTest extends CleanTestBase {

	private final Users _subject = new UsersImpl();

	
	@Test
	public void signup() throws Refusal {
		assertSignUp("ana", "ana@gmail.com", "ana123");
		assertSignUp("bruninho", "bruno@gmail.com", "brunox");
	}


	private void assertSignUp(String username, String email, String password) throws Refusal {
		User user = _subject.signup(username, email, password);
		assertEquals(username, user.username());
	}

}
