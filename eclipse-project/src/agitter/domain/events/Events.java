package agitter.domain.events;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;

public interface Events {

	@Transaction
	Event create(User user, String description, long datetime) throws Refusal;
	
	void transferEvents(User receivingEvents, User beingDropped);
	void delete(User user, Event event);

	Event get(long eventId);
	List<Event> toHappen(User user);
	List<Event> search(User user, String fragment);

}
