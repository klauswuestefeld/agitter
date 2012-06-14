package agitter.domain.users;

import basis.lang.exceptions.Refusal;
import agitter.common.Portal;
import agitter.domain.emails.EmailAddress;

public interface User {

	String name();
	void setName(String name);
	EmailAddress email();
	String screenName();
	String picture();
	
	boolean hasName();
	boolean hasSignedUp();
	boolean isPasswordCorrect(String passwordAttempt);
	void setPassword(String newPassword);
	void attemptToSetPassword(String currentPasswordAttempt, String newPassword) throws Refusal;

	boolean hasUnsubscribedFromEmails();
	void setUnsubscribedFromEmails(boolean interested);

	void linkAccount(Portal portal, String username, String oauthVerifier, String oauthToken, String imageProfile, String email);
	boolean isAccountLinked(Portal portal);
	void unlinkAccount(Portal portal);
	String linkedAccountUsername(Portal portal);
	String linkedAccountImage(Portal portal);
}
