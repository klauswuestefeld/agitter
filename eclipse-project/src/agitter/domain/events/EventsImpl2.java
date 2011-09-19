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

	private SortedSet<EventImpl2> _all = new TreeSet<EventImpl2>(new EventComparator());
	

	@Override
	public Event create(User user, String description, long datetime, List<Group> inviteeGroups, List<User> invitees) throws Refusal {
		assertIsInTheFuture(datetime);
		EventImpl2 event = new EventImpl2(user, description, datetime, inviteeGroups, invitees);
		_all.add(event);
		return event;
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
	

	private void assertIsInTheFuture(long datetime) throws Refusal {
		if(datetime < Clock.currentTimeMillis())
			throw new Refusal("Novos eventos devem ser criados com data futura.");
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
