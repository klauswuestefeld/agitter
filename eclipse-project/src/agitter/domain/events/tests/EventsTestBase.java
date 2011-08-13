package agitter.domain.events.tests;


import java.util.Arrays;
import java.util.Collections;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;
import agitter.domain.users.User;
import agitter.domain.users.tests.UsersTestBase;

public abstract class EventsTestBase extends UsersTestBase {

	protected final Events subject = new EventsImpl();
	protected final User ana = user("Ana", "ana@email.com", "123x");
	protected final User jose = user("Jose", "jose@email.com", "123x");
	
	
	protected Event createEvent(User owner, String description, long startTime, User... invitees) throws Refusal {
		return createEvent(subject, owner, description, startTime, invitees);
	}


	protected Event createEvent(Events events, User owner, String description, long startTime, User... invitees) throws Refusal {
		events.create(owner, description, startTime, Collections.EMPTY_LIST, Arrays.asList(invitees));
		for (Event event : events.toHappen(owner))
			if (event.description().equals(description) && event.datetime() == startTime)
				return event;
		throw new IllegalStateException("Newly created event not found.");
	}

	

}