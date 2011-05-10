package agitter.domain.users;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;

public interface Users {

	public class UserNotFound extends Refusal {  public UserNotFound(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }
	public class InvalidPassword extends Refusal {  public InvalidPassword(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }

	@Transaction
	User login(String emailOrUsername, String password) throws UserNotFound, InvalidPassword;

	@Transaction
	User signup(String username, String email, String password) throws Refusal;

}
