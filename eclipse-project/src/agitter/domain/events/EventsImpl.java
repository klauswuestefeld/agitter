package agitter.domain.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.emails.EmailDestination;
import agitter.domain.users.User;

public class EventsImpl implements Events {

	private static final int MAX_EVENTS_TO_SHOW = 40;

	private SortedSet<EventImpl> _all = new TreeSet<EventImpl>(new EventComparator());

	@Override
	public Event create(User user, String description, long datetime, List<Group> inviteeGroups, List<EmailAddress> inviteeEmails) throws Refusal {
		assertIsInTheFuture(datetime);
		EventImpl event = new EventImpl(user, description, datetime, inviteeGroups, inviteeEmails);
		_all.add(event);
		return event;
	}


	@Override
	public List<Event> toHappen(User user) {
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SHOW);
		final long currentDate = Clock.currentTimeMillis();

		for(EventImpl e : _all) {
			if (e.datetime() < currentDate) continue;
			if (!e.isVisibleTo(user)) continue;
			result.add(e);
			if (result.size() == MAX_EVENTS_TO_SHOW) break;
		}
		return result;
	}
	
	@Override
	public Map<EmailDestination, List<Event>> emailsAndItsEventsToHappenIn24Hours() {
		final long currentDate = Clock.currentTimeMillis();
		final long next24HoursDate = currentDate + 1000*60*60*24;
		for(EventImpl e: _all) {
			if (e.datetime() < currentDate) continue;
			if (e.datetime() > next24HoursDate) continue;
		}
		return null;
	}

	private void assertIsInTheFuture(long datetime) throws Refusal {
		if(datetime < Clock.currentTimeMillis())
			throw new Refusal("Novos eventos devem ser criados com data futura.");
	}



}
