package agitter.domain.users;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public interface Users {

	public class UserNotFound extends Refusal {  public UserNotFound(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }
	public class InvalidPassword extends Refusal {  public InvalidPassword(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }
	public class UserNotActive extends Refusal { public UserNotActive(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }

	List<User> all();

	@Transaction
	User signup(EmailAddress email, String password) throws Refusal;
	void activate(String email, String activationCode) throws Refusal;

	@Transaction /** Use searchByEmail before calling this to test if it is necessary and avoid a transaction. Returns existing user or creates a new one with that email address. */
	User produce(EmailAddress email);

	User loginWithEmail(EmailAddress email, String password) throws UserNotActive, UserNotFound, InvalidPassword;
	User findByEmail(EmailAddress email) throws UserNotFound;
	User searchByEmail(EmailAddress email);

	void unsubscribe(String userEncryptedInfo) throws UserNotFound;

}
