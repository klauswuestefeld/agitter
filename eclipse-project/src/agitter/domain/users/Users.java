package agitter.domain.users;

import org.prevayler.bubble.Transaction;
import sneer.foundation.lang.exceptions.Refusal;

public interface Users {

	public class UserNotFound extends Refusal {  public UserNotFound(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }
	public class InvalidPassword extends Refusal {  public InvalidPassword(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }

	User[] all();

	@Transaction
	User signup(String username, String email, String password) throws Refusal;

	User loginWithUsername(String username, String password) throws UserNotFound, InvalidPassword;
	User loginWithEmail(String email, String password) throws UserNotFound, InvalidPassword;
	User findByUsername(String username) throws UserNotFound;
	User findByEmail(String email) throws UserNotFound;

}
