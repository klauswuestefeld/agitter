package agitter.domain.users;

import agitter.domain.emails.EmailAddress;

public interface User {

	String name();
	void setName(String name);
	EmailAddress email();
	String screenName();

	boolean hasName();
	boolean hasSignedUp();
	boolean isPasswordCorrect(String passwordAttempt);
	void setPassword(String newPassword);

	boolean hasUnsubscribedFromEmails();
	void setUnsubscribedFromEmails(boolean interested);

}
