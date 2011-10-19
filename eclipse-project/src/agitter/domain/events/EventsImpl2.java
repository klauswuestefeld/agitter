package agitter.domain.events;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class EventsImpl2 implements Events {

	private static final int MAX_EVENTS_TO_SHOW = 40;
	private static final long TWO_HOURS = 1000 * 60 * 60 * 2;

	@SuppressWarnings("unused") @Deprecated transient private long nextId; //2011-10-19
	private SortedSet<EventImpl2> _all = new TreeSet<EventImpl2>(new EventComparator());

	
	@Override
	public Event create(User user, String description, long datetime, List<Group> inviteeGroups, List<User> invitees) throws Refusal {
		EventImpl2 event = new EventImpl2(user, description, datetime, inviteeGroups, invitees);
		_all.add(event);
		return event;
	}

	
	@Override
	public void setEventTime(Event event, long newTime) throws Refusal {
		EventImpl2 casted = (EventImpl2) event;
		boolean wasThere = _all.remove(casted); //Event could have been deleted.
		try {
			casted.setDatetime(newTime);
		} finally {
			if (wasThere) _all.add(casted);
		}
	}


	@Override
	public List<Event> toHappen(User user) {
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SHOW);
		final long twoHoursAgo = Clock.currentTimeMillis() - TWO_HOURS;

		for(EventImpl2 e : _all) {
			if (e.datetime() < twoHoursAgo) continue;
			if (!e.isVisibleTo(user)) continue;
			result.add(e);
			if (result.size() == MAX_EVENTS_TO_SHOW) break;
		}
		return result;
	}

	
	@Override
	public boolean isDeletableBy(Event event, User user) {
		return event.owner() == user;
	}


	@Override
	public void delete(Event event, User user) {
		if (!isDeletableBy(event, user))
			throw new IllegalArgumentException("Evento não deletável por este usuário.");
		_all.remove(event);
	}

}
