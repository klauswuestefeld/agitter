package agitter.domain.users;

import agitter.domain.emails.EmailAddress;


public class UserImpl implements User {

	private EmailAddress email;
	private String name;
	private String password;
	private boolean isSubscribedToEmails = true;

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
		return hasSignedUp() && password.equals(passwordAttempt);
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
	public boolean isSubscribedToEmails() {
		return isSubscribedToEmails;
	}

	@Override
	public void setSubscribedToEmails(boolean subscribed) {
		isSubscribedToEmails = subscribed;
	}


	@Override
	public String toString() {
		return email.toString();
	}

	@Override
	public boolean hasSignedUp() {
		return password != null;
	}


	void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String name() {
		return name;
	}


	@Override
	public void setName(String name) {
		this.name = name;
	}

}
