package agitter.domain.users;

import java.util.Random;

import agitter.domain.emails.EmailAddress;


public class UserImpl implements User {

	private EmailAddress email;
	private String password;
	private boolean isInterestedInPublicEvents = true;
	private boolean isActive = false;
	private Long activationCode = new Random().nextLong();

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
		return isActive;
	}

	@Override
	public Long activationCode() {
		return activationCode;
	}

	@Override
	public void activate() {
		isActive = true;
	}
	
}
