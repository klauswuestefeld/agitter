package agitter.domain.events;

import java.util.*;

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
	public List<EventOcurrence> toHappen(User user) {
		List<EventOcurrence> ret = new ArrayList<EventOcurrence>();
		for(Event e : _all) {
			if (!e.isVisibleTo(user)) continue;
			if (!e.isInterested(user)) continue;
			accumulateOccurences(ret, e, e.datetimesInterestingFor(user));
		}
		Collections.sort(ret);
		return ret;
	}
	@Override
	public List<EventOcurrence> search(User user, String fragment) {
		fragment = fragment.toLowerCase();
		List<EventOcurrence> ret = new ArrayList<EventOcurrence>();
		for (Event e : _all) {
			if (!e.isVisibleTo(user)) continue;
			if (!e.description().toLowerCase().contains(fragment)) continue;
			accumulateOccurences(ret, e, e.datetimes());
		}
		Collections.sort(ret);
		return ret;
	}

	private void accumulateOccurences(List<EventOcurrence> ret, Event e, long[] datetimes) {
		for(long ocurrence : datetimes)
			ret.add(new EventOcurrenceImpl(e, ocurrence));
	}

	@Override
	public void delete(User user, Event event) {
		if (!event.isEditableBy(user))
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
			e.transferOwnershipIfNecessary(receivingEvents, beingDropped);			
	}

}
