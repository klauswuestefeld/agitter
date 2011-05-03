package agitter.domain.events;

import java.util.SortedSet;

import agitter.domain.User;
import org.prevayler.bubble.Transaction;
import sneer.foundation.lang.exceptions.Refusal;

public interface Events {

	@Transaction
	PublicEvent create(User user, String description, long datetime) throws Refusal;

	SortedSet<PublicEvent> toHappen(User user);

}
