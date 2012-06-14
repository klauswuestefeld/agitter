package agitter.ui.presenter.hacks;

import basis.lang.exceptions.Refusal;
import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

public class AnnePreparation {

	private static final EmailAddress ANNE_USER_EMAIL = EmailAddress.certain("anne@demo.com");
	private final Agitter domain;
	private User user;


	public AnnePreparation(Agitter domain) {
		this.domain = domain;
	}
	
	public User user() {
		User existing = domain.users().searchByEmail(ANNE_USER_EMAIL);
		if (existing != null) return existing;

		user = signup(ANNE_USER_EMAIL);
		return user;
	}


	private User signup(EmailAddress email) {
		try {
			return domain.users().signup(email, "password123");
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}
}