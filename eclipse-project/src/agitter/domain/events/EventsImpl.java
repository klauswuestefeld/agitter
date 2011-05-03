package agitter.domain.events;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.User;

public class EventsImpl implements Events, Serializable {

	private static final long serialVersionUID = 1L;

	private SortedSet<Event> _all = new TreeSet<Event>( new EventComparator() );
	
	
	@Override
	public Event create(User user, String description, long datetime) throws Refusal {
		assertIsInTheFuture(datetime);
		
		Event event = new Event(description, datetime);
		_all.add(event);
		return event;
	}


	private void assertIsInTheFuture(long datetime) throws Refusal {
		if (datetime < Clock.currentTimeMillis())
			throw new Refusal("Novos eventos devem ser criados com data futura.");
	}

	
	@Override
	public SortedSet<Event> toHappen(User user) {
		return _all.tailSet(new Event("Dummy", Clock.currentTimeMillis()));
	}


	@Override
	public void remove(User user, Event event) {
		_all.remove(event);
	}

}
