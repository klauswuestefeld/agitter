package agitter.domain;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Events;

public interface Agitter {

	@Transaction
	User signup(String login, String name, String email, String password) throws Refusal;
	@Transaction
	User login(String loginOrEmail, String password) throws Refusal;
	
	Events events();

}
