package agitter.domain.users;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public interface Users {

	public class UserNotFound extends Refusal {  public UserNotFound(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }
	public class InvalidPassword extends Refusal {  public InvalidPassword(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }

	List<User> all();

	@Transaction
	User signup(EmailAddress email, String password) throws Refusal;
	@Transaction /** Use searchByEmail before calling this to test if it is necessary and avoid a transaction. Returns existing user or creates a new one with that email address. */
	User produce(EmailAddress email);
	void unsubscribe(String userEncryptedInfo) throws UserNotFound;

	User loginWithEmail(EmailAddress email, String password) throws UserNotFound, InvalidPassword;
	User findByEmail(EmailAddress email) throws UserNotFound;
	User searchByEmail(EmailAddress email);

	String userEncyptedInfo(User user);

}
