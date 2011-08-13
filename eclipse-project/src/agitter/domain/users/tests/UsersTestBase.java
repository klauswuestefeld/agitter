package agitter.domain.users.tests;

import static agitter.domain.emails.EmailAddress.mail;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.domain.users.Users;
import agitter.domain.users.UsersImpl;

public abstract class UsersTestBase extends CleanTestBase {

	public final Users users = new UsersImpl();

	protected User user(String email) throws Refusal {
		return UserUtils.produce(users, mail(email));
	}

	protected User user(String username, String email, String password) {
		try {
			return users.signup(username, mail(email), password);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

}