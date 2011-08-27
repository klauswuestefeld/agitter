package agitter.domain.users;

import agitter.domain.emails.EmailAddress;


@Deprecated
public class UserImpl implements User {

	String _username;
	String _email;
	String _password;
	boolean isInterestedInPublicEvents = true;

	
	@Override
	public String username() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public EmailAddress email() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public String fullName() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public String password() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public boolean isPassword(String attempt) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public boolean isInterestedInPublicEvents() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public void setInterestedInPublicEvents(boolean interested) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

}
