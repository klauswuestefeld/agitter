package agitter.domain.users;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;

public interface Users {

	@Transaction
	User signup(String username, String email, String password) throws Refusal;

	@Transaction
	User login(String email, String password) throws Refusal;

}
