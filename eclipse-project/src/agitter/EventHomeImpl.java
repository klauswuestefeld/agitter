package agitter;

import java.util.SortedSet;
import java.util.TreeSet;

import agitter.util.Clock;

public class EventHomeImpl implements EventHome {

	private Clock _clock;
	private SortedSet<Event> _all = new TreeSet<Event>( new EventComparator() );
	
	
	public EventHomeImpl(Clock clock) {
		_clock = clock;
	}

	
	@Override
	public Event create(String description, long datetime) {
		Event event = new Event(description, datetime);
		_all.add(event);
		return event;
	}

	
	@Override
	public SortedSet<Event> all() {
		return _all;
	}
	
	
	@Override
	public SortedSet<Event> toHappen() {
		return _all.tailSet(new Event("Dummy", _clock.datetime()));
	}

}
