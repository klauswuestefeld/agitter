package agitter.ui.presenter;

import static java.util.Collections.EMPTY_LIST;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
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

		user = signupDemoUser();
		populate();
		return user;
	}


	private User signupDemoUser() {
		try {
			return domain.users().signup(DEMO_USER_EMAIL, "demoPassword");
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}


	private void populate() {
		try {
			domain.events().create(user, "Party!", System.currentTimeMillis() + (1000*60*60*24*10), EMPTY_LIST, EMPTY_LIST);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

}