package agitter.domain.events.tests;

import java.util.Arrays;
import java.util.Collections;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;
import agitter.domain.users.User;
import agitter.domain.users.UserImpl;

public abstract class EventsTestBase extends CleanTestBase {

	protected final Events _subject = new EventsImpl();
	protected final User _ana = new UserImpl("Ana", "ana@email.com", "123x");
	protected final User _jose = new UserImpl("Jose", "jose@email.com", "123x");
	
	
	protected Event createEvent(User owner, String description, long startTime, EmailAddress... invitees) throws Refusal {
		return createEvent(_subject, owner, description, startTime, invitees);
	}


	protected Event createEvent(Events events, User owner, String description, long startTime, EmailAddress... invitees) throws Refusal {
		events.create(owner, description, startTime, Collections.EMPTY_LIST, Arrays.asList(invitees));
		for (Event event : events.toHappen(owner))
			if (event.description().equals(description) && event.datetime() == startTime)
				return event;
		throw new IllegalStateException("Newly created event not found.");
	}

	

}