package agitter.domain.events.tests;

import static agitter.domain.contacts.tests.ContactsTest.createGroup;

import org.junit.Test;

import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl2;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.events.Event;
import agitter.domain.users.User;

public class EventGuestsTest extends EventsTestBase {
	
	private final Contacts contacts = new ContactsImpl2();

	
	@Test
	public void emailInvitation() throws Exception {
		Event event = createEvent(ana, "Dinner at Joes", 1000);
		assertTrue(subject.toHappen(jose).isEmpty());
		event.addInvitee(user("jose@email.com"));
		assertFalse(subject.toHappen(jose).isEmpty());
		assertFalse(subject.toHappen(ana).isEmpty());
	}

	
	@Test
	public void groupInvitation() throws Exception {
		User user = ana;
		Event event = createEvent(user, "Dinner at Joes", 1000);
		
		ContactsOfAUser anasContacts = contacts.contactsOf(user);
		Group friends = createGroup(anasContacts, "Friends");
		anasContacts.addContactTo(friends, user("jose@email.com"));
		
		event.addInvitees(friends);
		assertFalse(subject.toHappen(jose).isEmpty());
	}

	
	@Test
	public void subgroupInvitation() throws Exception {
		Event event = createEvent(ana, "Dinner at Joes", 1000);
		
		ContactsOfAUser anasContacts = contacts.contactsOf(ana);
		Group family = createGroup(anasContacts, "Family");
		Group friends = createGroup(anasContacts, "Friends");
		Group best = createGroup(anasContacts, "Best_Friends");
		friends.addSubgroup(best);
		anasContacts.addContactTo(best, user("jose@email.com"));
		
		event.addInvitees(family);
		assertTrue(subject.toHappen(jose).isEmpty());

		event.addInvitees(friends);
		assertFalse(subject.toHappen(jose).isEmpty());
	}

}

