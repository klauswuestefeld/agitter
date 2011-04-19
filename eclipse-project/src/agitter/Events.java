package agitter;

import java.util.SortedSet;

public interface Events {

	Event create(String description, long datetime);
	SortedSet<Event> all();
	SortedSet<Event> toHappen();

}
