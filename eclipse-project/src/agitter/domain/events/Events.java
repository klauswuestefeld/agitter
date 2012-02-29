package agitter.domain.events;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;

public interface Events {

	@Transaction
	Event create(User user, String description, long datetime) throws Refusal;

	void setDescription(User user, Event event, String description) throws Refusal;
	
	boolean isEditableBy(User user, Event event);
	void delete(User user, Event event);

	List<Event> toHappen(User user);
	
	@Deprecated
	void setLastId(long nextId);

}
