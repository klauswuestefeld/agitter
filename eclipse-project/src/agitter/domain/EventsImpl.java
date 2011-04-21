package agitter.domain;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.Clock;

public class EventsImpl implements Events, Serializable {

	private static final long serialVersionUID = 1L;

	private SortedSet<Event> _all = new TreeSet<Event>( new EventComparator() );
	
	
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
		return _all.tailSet(new Event("Dummy", Clock.currentTimeMillis()));
	}


	@Override
	public void remove(Event event) {
		_all.remove(event);
	}

}
