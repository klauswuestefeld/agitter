package agitter.domain.users;

import java.util.Random;

import agitter.domain.emails.EmailAddress;


public class UserImpl implements User {

	private EmailAddress email;
	private String password;
	private boolean isInterestedInPublicEvents = true;
	@SuppressWarnings("unused") @Deprecated transient private boolean isActive = false; //Transient in 2011-09-14
	@SuppressWarnings("unused") @Deprecated transient private Long activationCode = new Random().nextLong(); //Transient in 2011-09-14

	public UserImpl(EmailAddress email, String password) {
		this.email = email;
		this.password = password;
	}


	@Override
	public EmailAddress email() {
		return email;
	}


	@Override
	public boolean isPasswordCorrect(String passwordAttempt) {
		return password.equals(passwordAttempt);
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
	public boolean isActive() {
		return password != null;
	}

}
