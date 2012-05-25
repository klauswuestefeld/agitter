package agitter.domain.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;

public class EventsImpl2 implements Events {

	private long lastId = 0;
	
	//Vitor: Important, don't assume _all is sorted by date. Multiple dates had broken this logic! 
	private SortedSet<EventImpl2> _all = new TreeSet<EventImpl2>(new EventComparator());
	
	@Override
	public Event create(User user, String description, long datetime) throws Refusal {
		EventImpl2 event = new EventImpl2(getNextId(), user, description, datetime);
		_all.add(event);
		return event;
	}


	private List<EventOcurrence> search(User user, String fragment, boolean onlyInteresting) {
		List<EventOcurrence> ret = new ArrayList<EventOcurrence>();
		for(Event e : _all)
			accumulateOccurences(ret, e, user, onlyInteresting, fragment);
		Collections.sort(ret);
		return ret;
	}

	
	private void accumulateOccurences(List<EventOcurrence> ret, Event e, User user, boolean onlyInteresting, String fragment) {
		if (!e.isVisibleTo(user)) return;
		if (onlyInteresting && !e.isInterested(user)) return;
		if (fragment != null && !e.description().toLowerCase().contains(fragment)) return;
		long[] datetimes = onlyInteresting
			? ((EventImpl2)e).datetimesToComeFilteredBy(user)
			: e.datetimes();
		accumulateOccurences(ret, e, datetimes);
	}


	@Override
	public List<EventOcurrence> toHappen(User user) {
		return search(user, null, true);
	}
	@Override
	public List<EventOcurrence> search(User user, String caseInsensitiveFragment) {
		String fragment = caseInsensitiveFragment.toLowerCase();
		return search(user, fragment, false);
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
