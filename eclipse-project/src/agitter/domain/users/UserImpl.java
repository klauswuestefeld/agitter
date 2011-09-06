package agitter.domain.users;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;


public class UserImpl implements User {

	//Made transient on 2011-09-06
	private transient String _username;
	private transient String username;
	private transient String _email;
	private transient String _password;

	private EmailAddress email;
	private String password;
	private boolean isInterestedInPublicEvents = true;

	public UserImpl(EmailAddress email, String password) {
		this.email = email;
		this.password = password;
	}


	@Override
	public EmailAddress email() {
		return email;
	}


	@Override
	public boolean isPassword(String attempt) {
		return password.equals(attempt);
	}


	@Override
	public String password() {
		return password;
	}

	@Override
	public String screenName() {
		return email().toString();
	}

	@Override
	public boolean isInterestedInPublicEvents() {
		return isInterestedInPublicEvents;
	}

	@Override
	public void setInterestedInPublicEvents(boolean interested) {
		isInterestedInPublicEvents = interested;
	}


	@Override
	public String toString() {
		return email.toString();
	}


	@Override
	public int hashCode() {
		return email == null
			? _email.hashCode()
			: email.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		return obj instanceof User
			? email.equals(((User)obj).email())
			: false;
	}


	public void migrate() {
		try {
			email = EmailAddress.mail(_email);
		} catch (Refusal e) {
			try {
				System.out.println("Usuario: " + _username + " com email bichado: " + _email);
				email = EmailAddress.mail("foo" + System.nanoTime() + "@mail.com");
			} catch (Refusal e1) {
				throw new IllegalStateException(e1);
			}
		}
		password = _password;
		username = _username;
	}

	
	
}
