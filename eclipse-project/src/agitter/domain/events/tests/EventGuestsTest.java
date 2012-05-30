package agitter.domain.events.tests;

import static agitter.domain.contacts.tests.ContactsTest.createGroup;

import org.junit.Test;

import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.events.Event;
import agitter.domain.events.Events.InvitationToSendOut;
import agitter.domain.users.User;

public class EventGuestsTest extends EventsTestBase {
	
	private final Contacts contacts = agitter.contacts();

	
	@Test
	public void userInvitation() throws Exception {
		Event event = createEvent(ana, "Dinner at Joes", 1000);
		assertTrue(subject.toHappen(jose).isEmpty());
		event.invite(ana, jose);
		assertFalse(subject.toHappen(jose).isEmpty());
		assertFalse(subject.toHappen(ana).isEmpty());
		
		assertInvitationToSendOut(jose, event);
	}

	
	@Test
	public void groupInvitation() throws Exception {
		Event event = createEvent(ana, "Dinner at Joes", 1000);
		
		ContactsOfAUser anasContacts = contacts.contactsOf(ana);
		Group friends = createGroup(anasContacts, "Friends");
		anasContacts.addContactTo(friends, jose);
		
		event.invite(ana, friends);
		assertFalse(subject.toHappen(jose).isEmpty());

		//assertInvitationToSendOut(jose, event);
	}

	
	@Test
	public void subgroupInvitation() throws Exception {
		Event event = createEvent(ana, "Dinner at Joes", 1000);
		
		ContactsOfAUser anasContacts = contacts.contactsOf(ana);
		Group family = createGroup(anasContacts, "Family");
		Group friends = createGroup(anasContacts, "Friends");
		Group best = createGroup(anasContacts, "Best_Friends");
		friends.addSubgroup(best);
		anasContacts.addContactTo(best, jose);
		
		event.invite(ana, family);
		assertTrue(subject.toHappen(jose).isEmpty());

		event.invite(ana, friends);
		assertFalse(subject.toHappen(jose).isEmpty());

	//	assertInvitationToSendOut(jose, event);
	}
	
	
	private void assertInvitationToSendOut(User invitee, Event event) {
		InvitationToSendOut invitation = subject.nextInvitationToSendOut();
		assertNotNull(invitation);
		assertSame(invitee, invitation.invitee());
		assertSame(event, invitation.event());
	}

}

