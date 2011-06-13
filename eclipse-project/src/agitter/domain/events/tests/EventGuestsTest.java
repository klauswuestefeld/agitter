package agitter.domain.events.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.EventImpl;

public class EventGuestsTest extends EventsTestBase {
	
	@BeforeClass
	public static void beforeEventsGests() {
		EventImpl.PRIVATE_EVENTS_ON = true;
	}
	
	@AfterClass
	public static void afterEventsGests() {
		EventImpl.PRIVATE_EVENTS_ON = false;
	}
	

	@Test
	public void addNew() throws Exception {
		Event event = _subject.create(_ana, "Dinner at Joes", 1000);
		assertTrue(_subject.toHappen(_jose).isEmpty());
		event.addInvitation(new EmailAddress("jose@email.com"));
		assertFalse(_subject.toHappen(_jose).isEmpty());
		assertFalse(_subject.toHappen(_ana).isEmpty());
	}
	
	
}
