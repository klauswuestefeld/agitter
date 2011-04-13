package agitter.server.domain.comparators;


import java.util.Comparator;

import agitter.server.domain.Event;

public class EventDateTimeComparator implements Comparator<Event> {

	@Override
	public int compare(Event event1, Event event2) {
		return event1.getDate().compareTo(event2.getDate());
	}

}
