package agitter.domain.events.tests;

import static agitter.domain.contacts.tests.ContactsTest.createGroup;

import org.junit.Test;

import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.users.User;

public class EventGuestsTest extends EventsTestBase {
	
	private final Contacts contacts = new ContactsImpl();

	
	@Test
	public void emailInvitation() throws Exception {
		Event event = createEvent(_ana, "Dinner at Joes", 1000);
		assertTrue(_subject.toHappen(_jose).isEmpty());
		event.addInvitee(new EmailAddress("jose@email.com"));
		assertFalse(_subject.toHappen(_jose).isEmpty());
		assertFalse(_subject.toHappen(_ana).isEmpty());
	}

	
	@Test
	public void groupInvitation() throws Exception {
		User user = _ana;
		Event event = createEvent(user, "Dinner at Joes", 1000);
		
		ContactsOfAUser anasContacts = contacts.contactsOf(user);
		Group friends = createGroup(anasContacts, "Friends");
		anasContacts.addContactTo(friends, new EmailAddress("jose@email.com"));
		
		event.addInvitees(friends);
		assertFalse(_subject.toHappen(_jose).isEmpty());
	}

	
	@Test
	public void subgroupInvitation() throws Exception {
		Event event = createEvent(_ana, "Dinner at Joes", 1000);
		
		ContactsOfAUser anasContacts = contacts.contactsOf(_ana);
		Group family = createGroup(anasContacts, "Family");
		Group friends = createGroup(anasContacts, "Friends");
		Group best = createGroup(anasContacts, "Best Friends");
		friends.addSubgroup(best);
		anasContacts.addContactTo(best, new EmailAddress("jose@email.com"));
		
		event.addInvitees(family);
		assertTrue(_subject.toHappen(_jose).isEmpty());

		event.addInvitees(friends);
		assertFalse(_subject.toHappen(_jose).isEmpty());
	}

}

