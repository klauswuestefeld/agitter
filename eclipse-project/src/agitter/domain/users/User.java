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

	void linkAccount(String portal, String username, String oauth_verifier,	String oauth_token);
	String linkedAccountUsername(String string);
	boolean linkedAccount(String string);
	void unlinkAccount(String twitter);
}
