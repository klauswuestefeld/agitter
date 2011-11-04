package agitter.uiold.presenter;

import java.util.Arrays;

import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import sneer.foundation.lang.exceptions.Refusal;

import static java.util.Collections.EMPTY_LIST;

public class DemoPreparation {

	private static final EmailAddress DEMO_USER_EMAIL = EmailAddress.certain("demo@demo.com");
	private final Agitter domain;
	private User user;


	DemoPreparation(Agitter domain) {
		this.domain = domain;
	}
	
	User user() {
		User existing = domain.users().searchByEmail(DEMO_USER_EMAIL);
		if (existing != null) return existing;

		user = signup(DEMO_USER_EMAIL);
		populate();
		return user;
	}


	private User signup(EmailAddress email) {
		try {
			return domain.users().signup(email, "password123");
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}


	private void populate() {
		User anne = signup(EmailAddress.certain("anne@demo.com"));
		createEvent(user, "Party!", 1000*60*60*24*10, anne);
		createEvent(anne, "Movie", 1000*60*60*24*11, user);
	}

	private void createEvent(User owner, String description, int timeFromNow, User... invitees) {
		try {
			domain.events().create(owner, description, System.currentTimeMillis() + timeFromNow, EMPTY_LIST, Arrays.asList(invitees));
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

}