package agitter.domain.events;

import java.util.List;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import basis.lang.exceptions.Refusal;

import agitter.domain.users.User;

public class EventsImpl2 implements Events, EventImpl2.Boss {

	private long lastId = 0;
	
	//Vitor: Important, don't assume _all is sorted by date. Multiple dates have broken this logic! 
	private final SortedSet<EventImpl2> _all = new TreeSet<EventImpl2>(new EventComparator());

	private final Queue<InvitationToSendOut> invitationsToSendOut = new ConcurrentLinkedQueue<InvitationToSendOut>();
	
	
	@Override
	public Event create(User user, String description, long datetime) throws Refusal {
		EventImpl2 event = new EventImpl2(getNextId(), user, description, datetime, this);
		_all.add(event);
		return event;
	}


	@Override
	public List<EventOcurrence> toHappen(User user) {
		return new OccurrenceSelector(user, _all) { @Override long[] datetimesToVisit(Event e, User user) {
			if (!e.isInterested(user)) return null;
			return ((EventImpl2)e).datetimesToComeFilteredBy(user);
		}}.result;

	}

	
	@Override
	public List<EventOcurrence> search(User user, String caseInsensitiveFragment) {
		final String fragment = caseInsensitiveFragment.toLowerCase();
		return new OccurrenceSelector(user, _all) { @Override long[] datetimesToVisit(Event e, User user) {
			if (!e.description().toLowerCase().contains(fragment)) return null;
			return e.datetimes();
		}}.result;
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


	@Override
	public InvitationToSendOut popInvitationToSendOut() {
		return invitationsToSendOut.poll();
	}


	@Override
	public void onInvitationToSendOut(User invitee, Event event) {
		invitationsToSendOut.add(new InvitationToSendOutImpl(invitee, event));
	}

}
