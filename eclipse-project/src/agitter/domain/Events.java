package agitter.domain;

import java.util.SortedSet;

import org.prevayler.bubble.Transaction;

public interface Events {

	@Transaction
	Event create(String description, long datetime);
	
	SortedSet<Event> all();
	SortedSet<Event> toHappen();
	void remove(Event event);

}
