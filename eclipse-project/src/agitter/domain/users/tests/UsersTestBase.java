package agitter.domain.users.tests;

import static agitter.domain.emails.EmailAddress.mail;

import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;

public abstract class UsersTestBase extends CleanTestBase {

	public final Agitter agitter = new AgitterImpl();

	protected User user(String email) throws Refusal {
		return UserUtils.produce(agitter.users(), mail(email));
	}

	protected User user(String username, String email, String password) {
		try {
			return agitter.users().signup(username, mail(email), password);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

}