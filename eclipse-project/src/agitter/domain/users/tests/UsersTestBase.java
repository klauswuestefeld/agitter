package agitter.domain.users.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.util.logging.Level;
import java.util.logging.Logger;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.TestWithMocks;
import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;

public abstract class UsersTestBase extends TestWithMocks {

	{
		Logger.getLogger("").setLevel(Level.OFF);
	}
	public final Agitter agitter = new AgitterImpl();

	protected User user(String email) throws Refusal {
		return UserUtils.produce(agitter.users(), email(email));
	}

	protected User user(String username, String email, String password) {
		try {
			return agitter.users().signup(email(email), password);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

}