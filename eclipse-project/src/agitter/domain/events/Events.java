package agitter.domain.events;

import java.util.List;

import agitter.domain.users.User;

import org.prevayler.bubble.Transaction;
import sneer.foundation.lang.exceptions.Refusal;

public interface Events {

	@Transaction
	Event create(User user, String description, long datetime) throws Refusal;

	List<Event> toHappen(User user);

}
