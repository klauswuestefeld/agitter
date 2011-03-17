package guardachuva.agitos.server.domain.comparators;

import guardachuva.agitos.server.domain.Event;

import java.util.Comparator;

public class EventDateTimeComparator implements Comparator<Event> {

	@Override
	public int compare(Event event1, Event event2) {
		return event1.getDate().compareTo(event2.getDate());
	}

}
