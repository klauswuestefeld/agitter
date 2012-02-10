package agitter.domain.users;

import agitter.domain.emails.EmailAddress;

public interface User {

	public String name();
	public void setName(String name);
	public EmailAddress email();
	public String screenName();

	boolean hasSignedUp();
	public String password();
	public boolean isPasswordCorrect(String passwordAttempt);

	public boolean isInterestedInPublicEvents();
	public void setInterestedInPublicEvents(boolean interested);

}
