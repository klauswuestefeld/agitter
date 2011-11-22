package agitter.domain.events.tests;


import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.domain.users.tests.UsersTestBase;

public abstract class EventsTestBase extends UsersTestBase {

	protected final Events subject = agitter.events();
	protected final User ana = user("Ana", "ana@email.com", "123x");
	protected final User jose = user("Jose", "jose@email.com", "123x");
	
	
	protected Event createEvent(User owner, String description, long startTime, User... invitees) throws Refusal {
		return createEvent(subject, owner, description, startTime, invitees);
	}


	protected Event createEvent(Events events, User owner, String description, long startTime, User... invitees) throws Refusal {
		Event event = events.create(owner, description, startTime);
		for (User user : invitees)
			event.addInvitee(user);
		for (Event candidate : events.toHappen(owner))
			if (candidate.description().equals(description) && candidate.datetime() == startTime)
				return candidate;
		throw new IllegalStateException("Newly created event not found.");
	}

	

}