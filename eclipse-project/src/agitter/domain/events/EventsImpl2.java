package agitter.domain.events;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;

public class EventsImpl2 implements Events {

	private static final int MAX_EVENTS_TO_SHOW = 40;
	private static final long TWO_HOURS = 1000 * 60 * 60 * 2;

	@SuppressWarnings("unused") @Deprecated transient private long nextId; //2011-10-19
	private SortedSet<EventImpl2> _all = new TreeSet<EventImpl2>(new EventComparator());

	
	@Override
	public Event create(User user, String description, long datetime) throws Refusal {
		EventImpl2 event = new EventImpl2(user, description, datetime);
		if (_all.contains(event))
			throw new DuplicateEvent();
		_all.add(event);
		return event;
	}

	
	@Override
	public void setDatetime(User user, Event event, long datetime) throws Refusal {
		edit(user, event, event.description(), datetime);
	};

	
	@Override
	public void setDescription(User user, Event event, String description) throws Refusal {
		edit(user, event, description, event.datetimes()[0]);
	};
	
	
	private void edit(User user, Event event, String newDescription, long newDatetime) throws Refusal {
		if (!isEditableBy(user, event)) throw new IllegalStateException("Event not editable by this user.");
		EventImpl2 casted = (EventImpl2) event;
		boolean wasThere = _all.remove(casted); //Event could have been deleted.
		try {
			casted.edit(newDescription, newDatetime);
		} finally {
			if (wasThere) _all.add(casted);
		}
	}


	@Override
	public List<Event> toHappen(User user) {
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SHOW);
		final long twoHoursAgo = Clock.currentTimeMillis() - TWO_HOURS;

		for(EventImpl2 e : _all) {
			if (e.datetimes().length > 0 && e.datetimes()[0] < twoHoursAgo) continue;
			if (!e.isVisibleTo(user)) continue;
			result.add(e);
			if (result.size() == MAX_EVENTS_TO_SHOW) break;
		}
		return result;
	}

	
	@Override
	public boolean isEditableBy(User user, Event event) {
		return event.owner() == user;
	}


	@Override
	public void delete(User user, Event event) {
		if (!isEditableBy(user, event))
			throw new IllegalArgumentException("Agito não deletável por este usuário.");
		_all.remove(event);
	}


	public void migrateSchemaIfNecessary() {
		for (Event e : _all) 
			((EventImpl2)e).migrateSchemaIfNecessary();
		
	}

}
