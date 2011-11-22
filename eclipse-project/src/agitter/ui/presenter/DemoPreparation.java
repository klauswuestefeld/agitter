package agitter.ui.presenter;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.users.User;

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
		Event event;
		try {
			event = domain.events().create(owner, description, System.currentTimeMillis() + timeFromNow);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
		for (User user : invitees)
			event.addInvitee(user);
	}

}