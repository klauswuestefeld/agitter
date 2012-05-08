package agitter.domain.events;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;

public class EventsImpl2 implements Events {
	private static final long TWO_HOURS = 1000 * 60 * 60 * 2;
	
	private long lastId = 0;
	
	//Vitor: Important, don't assume _all is sorted by date. Multiple dates had broken this logic! 
	private SortedSet<EventImpl2> _all = new TreeSet<EventImpl2>(new EventComparator());
	
	@Override
	public Event create(User user, String description, long datetime) throws Refusal {
		EventImpl2 event = new EventImpl2(getNextId(), user, description, datetime);
		_all.add(event);
		return event;
	}
	
	@Override
	public void setDescription(User user, Event event, String description) throws Refusal {
		edit(user, event, description, event.datetimes());
	};
	
	private void edit(User user, Event event, String newDescription, long[] newDatetimes) throws Refusal {
		if (!isEditableBy(user, event)) throw new IllegalStateException("Event not editable by this user.");
		EventImpl2 casted = (EventImpl2) event;
		boolean wasThere = _all.remove(casted); //Event could have been deleted.
		try {
			casted.edit(newDescription, newDatetimes);
		} finally {
			if (wasThere) _all.add(casted);
		}
	}

	private boolean willHappen(Event e) {
		final long twoHoursAgo = Clock.currentTimeMillis() - TWO_HOURS;
		
		for (long datetime : e.datetimes()) { 
			if (datetime >= twoHoursAgo)
				return true;
		}
		return false;
	}

	@Override
	public List<Event> toHappen(User user) {		
		List<Event> result = new ArrayList<Event>();
		for(Event e : _all) {
			if (!willHappen(e)) continue;
			if (!e.isVisibleTo(user)) continue;
			result.add(e);
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


	private long getNextId() {
		return ++lastId;
	}

	@Override
	public Event get(long eventId) {
		for (Event e: _all)
			if (e.getId() == eventId)
				return e;
				
		return null;
	}
	
	@Override
	public void transferEvents(User receivingEvents, User beingDropped) {
		for (Event e: _all)
			e.replace(beingDropped, receivingEvents);			
	}

	@Override
	public List<Event> search(User user, String fragment) {
		fragment = fragment.toLowerCase();
		List<Event> ret = new ArrayList<Event>();
		for (Event e : _all) {
			if (!e.description().toLowerCase().contains(fragment)) continue; //Which is faster?
			if (!e.isVisibleTo(user)) continue; //Which is faster?
			ret.add(e);
		}
		return ret;
	}
}
