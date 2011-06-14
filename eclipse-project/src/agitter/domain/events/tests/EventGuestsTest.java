package agitter.domain.events.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import agitter.domain.contacts.ContactsImpl;
import agitter.domain.contacts.Group;
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
	public void emailInvitation() throws Exception {
		Event event = _subject.create(_ana, "Dinner at Joes", 1000);
		assertTrue(_subject.toHappen(_jose).isEmpty());
		event.addInvitation(new EmailAddress("jose@email.com"));
		assertFalse(_subject.toHappen(_jose).isEmpty());
		assertFalse(_subject.toHappen(_ana).isEmpty());
	}

	
	@Test
	public void groupInvitation() throws Exception {
		Event event = _subject.create(_ana, "Dinner at Joes", 1000);
		assertTrue(_subject.toHappen(_jose).isEmpty());
		
		Group all = new ContactsImpl().allContactsFor(_ana);
		all.addContact(new EmailAddress("jose@email.com"));
		
		event.addInvitation(all);
		assertFalse(_subject.toHappen(_jose).isEmpty());
	}

	
	@Test
	public void subgroupInvitation() throws Exception {
		Event event = _subject.create(_ana, "Dinner at Joes", 1000);
		
		Group all = new ContactsImpl().allContactsFor(_ana);
		Group friends = all.addSubgroup("Friends");
		Group family = all.addSubgroup("Family");
		friends.addContact(new EmailAddress("jose@email.com"));
		
		event.addInvitation(family);
		assertTrue(_subject.toHappen(_jose).isEmpty());

		event.addInvitation(friends);
		assertFalse(_subject.toHappen(_jose).isEmpty());
	}

	
}
