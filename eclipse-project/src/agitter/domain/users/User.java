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

	String linkedAccountUsername(String portal);
	String linkedAccountImage(String portal);
	boolean isAccountLinked(String portal);
	void unlinkAccount(String portal);
	void linkAccount(String portal, String username, String oauthVerifier,	String oauthToken, String imageProfile, String email);
}
