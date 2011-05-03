package agitter.domain.events;

import java.util.SortedSet;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.User;

public interface Events {

	@Transaction
	PublicEvent create(User user, String description, long datetime) throws Refusal;
	
	SortedSet<PublicEvent> toHappen(User user);
//	void remove(User user, PublicEvent publicEvent);

}
