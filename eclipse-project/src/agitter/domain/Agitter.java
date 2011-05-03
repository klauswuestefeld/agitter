package agitter.domain;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Events;

public interface Agitter {

	@Transaction
	public User signup(String name, String email, String password) throws Refusal;
	@Transaction
	public User login(String email, String password) throws Refusal;
	
	public Events events();

}
