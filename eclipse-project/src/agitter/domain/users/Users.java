package agitter.domain.users;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;

public interface Users {

	public class UserNotFound extends Refusal {  public UserNotFound(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }
	public class InvalidPassword extends Refusal {  public InvalidPassword(String veryHelpfulMessage) { super(veryHelpfulMessage); }  }

	@Transaction
	User signup(String username, String email, String password) throws Refusal;

	@Transaction
	User loginWithUsername(String username, String password) throws UserNotFound, InvalidPassword;

	@Transaction
	User loginWithEmail(String email, String password) throws UserNotFound, InvalidPassword;
	
	@Transaction
	User findByUsername(String username) throws UserNotFound;

	@Transaction
	User findByEmail(String email) throws UserNotFound;

}
