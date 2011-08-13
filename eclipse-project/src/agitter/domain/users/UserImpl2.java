package agitter.domain.users;

import agitter.domain.emails.EmailAddress;


public class UserImpl2 implements User {

	private final String username;
	private final EmailAddress email;
	private final String password;
	private boolean isInterestedInPublicEvents = true;

	public UserImpl2(String username, EmailAddress email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}


	@Override
	public String username() {
		return username;
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
	public String fullName() {
		return username();
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
		return username == null
			? email.toString()
			: username;
	}


	@Override
	public int hashCode() {
		return email.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		return obj instanceof User
			? email.equals(((User)obj).email())
			: false;
	}

	
	
}
