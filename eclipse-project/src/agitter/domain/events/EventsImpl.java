package agitter.domain.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.User;

public class EventsImpl implements Events, Serializable {

	private static final long serialVersionUID = 1L;
	private static final int MAX_EVENTS_TO_SHOW = 40;

	private SortedSet<EventImpl> _all = new TreeSet<EventImpl>( new EventComparator() );

	@Override
	public Event create(User user, String description, long datetime) throws Refusal {
		assertIsInTheFuture(datetime);
		EventImpl event = new EventImpl(user, description, datetime);
		_all.add(event);
		return event;
	}


	@Override
	public List<Event> toHappen(User user) {
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SHOW);
		final long currentDate = Clock.currentTimeMillis();

		for(EventImpl e : _all) {
			if (e.datetime() < currentDate) continue;
			if (!e.isInterested(user)) continue;
			result.add(e);
			if (result.size()==MAX_EVENTS_TO_SHOW) break;
		}
		return result;
	}

	private void assertIsInTheFuture(long datetime) throws Refusal {
		if (datetime < Clock.currentTimeMillis())
			throw new Refusal("Novos eventos devem ser criados com data futura.");
	}

}
