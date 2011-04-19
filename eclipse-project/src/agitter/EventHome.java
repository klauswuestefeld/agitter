package agitter;

import java.util.SortedSet;

public interface EventHome {

	Event create(String description, long datetime);
	SortedSet<Event> all();
	SortedSet<Event> toHappen();

}
