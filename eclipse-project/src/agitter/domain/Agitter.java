package agitter.domain;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;

public interface Agitter {

	@Transaction
	AgitterSession signup(String name, String email, String password);
	@Transaction
	AgitterSession login(String email, String password) throws Refusal;

}
