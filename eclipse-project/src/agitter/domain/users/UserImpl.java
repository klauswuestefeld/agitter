package agitter.domain.users;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;


public class UserImpl implements User {

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

}
