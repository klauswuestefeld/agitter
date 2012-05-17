package agitter.domain.events.tests;


import java.util.ArrayList;
import java.util.List;

import agitter.domain.events.EventOcurrence;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.domain.users.tests.UsersTestBase;

public abstract class EventsTestBase extends UsersTestBase {

	protected final Events subject = agitter.events();
	protected final User ana = user("Ana", "ana@email.com", "123x");
	protected final User jose = user("Jose", "jose@email.com", "123x");
	protected final User paulo = user("Paulo", "paulo@email.com", "123x");
	protected final User pedro = user("Pedro", "pedero@email.com", "123x");
	
	
	protected Event createEvent(User owner, String description, long startTime, User... invitees) throws Refusal {
		return createEvent(subject, owner, description, startTime, invitees);
	}


	protected Event createEvent(Events events, User owner, String description, long startTime, User... invitees) throws Refusal {
		Event event = events.create(owner, description, startTime);
		for (User user : invitees)
			event.invite(owner, user);
		for (EventOcurrence candidate : events.toHappen(owner))
			if (candidate.event().description().equals(description) && candidate.event().datetimes()[0] == startTime)
				return candidate.event();
		throw new IllegalStateException("Newly created event not found.");
	}


	protected Event createEvent(Events events, User owner, String description, long... dates) throws Refusal {
		Event event = events.create(owner, description, dates[0]);
		for (int i=1; i<dates.length; i++) 
			event.addDate(dates[i]);
		for (EventOcurrence candidate : events.toHappen(owner))
			if (candidate.event().description().equals(description) && candidate.event().datetimes()[0] == dates[0])
				return candidate.event();
		throw new IllegalStateException("Newly created event not found.");
	}

	protected List<Event> toEvents(List<EventOcurrence> eventOcurrences) {
		List<Event> ret = new ArrayList<Event>();
		for(EventOcurrence occ : eventOcurrences)
			ret.add(occ.event());
		return ret;
	}



}