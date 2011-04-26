package agitter.domain.events;

import java.util.SortedSet;

import org.prevayler.bubble.Transaction;


import sneer.foundation.lang.exceptions.Refusal;

public interface Events {

	@Transaction
	Event create(String description, long datetime) throws Refusal;
	
	SortedSet<Event> all();
	SortedSet<Event> toHappen();
	void remove(Event event);

}
