package agitter.domain.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import agitter.domain.User;

public class AgitterTest extends CleanTestBase {

	private final Agitter _subject = new AgitterImpl();

	
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
