package agitter;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import agitter.util.AgitterClock;

public class EventsImpl implements Events, Serializable {

	private static final long serialVersionUID = 1L;

	private AgitterClock _clock;
	private SortedSet<Event> _all = new TreeSet<Event>( new EventComparator() );
	
	
	public EventsImpl(AgitterClock clock) {
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


	@Override
	public void remove(Event event) {
		_all.remove(event);
	}

}
