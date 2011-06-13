package agitter.domain.events.tests;

import org.junit.Ignore;
import org.junit.Test;

import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;

public class EventGuestsTest extends EventsTestBase {

	@Ignore
	@Test
	public void addNew() throws Exception {
		
		Event event = _subject.create(_ana, "Dinner at Joes", 1000);
		assertTrue(_subject.toHappen(_jose).isEmpty());
		event.addInvitation(new EmailAddress("jose@email.com"));
		assertFalse(_subject.toHappen(_jose).isEmpty());
	}
	
	
}
