package agitter.domain.events.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.events.Event;
import agitter.domain.events.Invitation;
import agitter.domain.users.User;

public class EventsTest extends EventsTestBase {

	private static final long TWO_HOURS = 1000 * 60 * 60 * 2;

	
	@Test
	public void addNew() throws Exception {
		createEvent(ana, "Dinner at Joes", 1000);
		assertEquals(1, subject.toHappen(ana).size());
		Event event = subject.toHappen(ana).iterator().next();
		assertEquals("Dinner at Joes", event.description());
		assertEquals(1000, event.datetimes()[0]);
	}
	

	@Test
	public void duplicateEvent() throws Exception {
		Event event1 = createEvent(ana, "Dinner at Joes", 1000);
		Event event2 = createEvent(ana, "Dinner at Joes", 1000);
		assertNotNull(event1);
		assertNotNull(event2);
	}

	@Test
	public void differentEventsDontCauseDuplicateEventException() throws Exception {
		createEvent(ana, "Dinner at Joes", 1000);
		createEvent(jose, "Dinner at Joes", 1000);
		createEvent(ana, "Dinner at Moes", 1000);
		createEvent(ana, "Dinner at Joes", 1001);
	}


	@Ignore
	@Test
	public void changingEventTime() throws Refusal {
		Event firstEvent = createEvent(ana, "D1", 11);
		Event secondEvent = createEvent(ana, "D2", 12);
		Event thirdEvent = createEvent(ana, "D3", 13);

		secondEvent.addDate(14);
		secondEvent.addDate(15);
		secondEvent.addDate(16);
		secondEvent.removeDate(12);

		assertEquals(3, subject.toHappen(ana).size());
		assertSame(firstEvent, subject.toHappen(ana).get(0));
		assertSame(thirdEvent, subject.toHappen(ana).get(1));
		assertSame(secondEvent, subject.toHappen(ana).get(2));		
	}

	@Test
	public void recurrentDatesTest() throws Refusal {
		Event firstEvent = createEvent(ana, "D1", 11);
		Event secondEvent = createEvent(ana, "D2", 15);
		Event thirdEvent = createEvent(ana, "D3", 18);
		
		firstEvent.addDate(17);
		firstEvent.addDate(14);
		
		assertSame(firstEvent, subject.toHappen(ana).get(0));
		assertSame(secondEvent, subject.toHappen(ana).get(1));
		assertSame(thirdEvent, subject.toHappen(ana).get(2));			
	}
	

	@Test
	public void createAndEditEventWithGroups() throws Refusal {
		ContactsOfAUser contacts = agitter.contacts().contactsOf( ana );
		Group work = contacts.createGroup( "work" );
		
		Event event =  subject.create(ana, "Churras", 12);
		event.invite(ana, work);
		assertEquals(0, event.allResultingInvitees().length);

		Group friends = contacts.createGroup( "friends" );
		event.invite(ana, friends);
		event.invite(ana, jose);
		event.invite(jose, paulo);
		assertEquals(2, event.allResultingInvitees().length);
		assertContentsInAnyOrder(Arrays.asList( event.allResultingInvitees()),jose, paulo);
		
		contacts.addContactTo(friends,pedro);
		assertEquals(3, event.allResultingInvitees().length);
		assertContentsInAnyOrder(Arrays.asList(event.allResultingInvitees()), jose, paulo, pedro);
		
		event.invite(ana, friends);
		
		assertEquals(3, event.allResultingInvitees().length);
		assertContentsInAnyOrder(Arrays.asList(event.allResultingInvitees()), jose, paulo, pedro);
	}
	
	@Test
	public void testInvitationTree() throws Refusal {	
		Event event =  subject.create(ana, "Churras", 12);
		event.invite(ana, jose);
		assertEquals(1, event.allResultingInvitees().length);

		event.invite(jose, paulo);
		event.invite(jose, pedro);
		
		assertEquals(3, event.allResultingInvitees().length);
		assertContentsInAnyOrder(Arrays.asList(event.allResultingInvitees()), jose, paulo, pedro);
		
		assertEquals(ana,  event.owner());
		assertEquals(ana,  event.invitationTree().host());
		assertEquals(jose, event.invitationTree().invitees()[0].host());
		
		Invitation[] inviteesByJose = event.invitationTree().invitees()[0].invitees();
		List<User> invitees = new ArrayList<User>();
		for (Invitation i : inviteesByJose) {
			invitees.add(i.host());
		}
		assertContentsInAnyOrder(invitees, paulo, pedro);
		
		assertEquals(null, event.invitationTree().userThatInvited(ana));
		assertEquals(ana, event.invitationTree().userThatInvited(jose));
		assertEquals(jose, event.invitationTree().userThatInvited(pedro));
		assertEquals(jose, event.invitationTree().userThatInvited(paulo));
	}
	
	@Test
	public void testInvitationTreeDuplicatedUsers() throws Refusal {	
		Event event =  subject.create(ana, "Churras 2", 12);
		event.invite(ana, jose);
		assertEquals(1, event.allResultingInvitees().length);

		event.invite(jose, ana);
		event.invite(jose, pedro);
		
		assertEquals(2, event.allResultingInvitees().length);
		assertContentsInAnyOrder(Arrays.asList(event.allResultingInvitees()), jose, pedro);
		
		Event event2 =  subject.create(jose, "Churras 2", 12);
		event2.invite(ana, jose);	
		assertEquals(0, event2.allResultingInvitees().length);

		event2.invite(jose, ana);
		event2.invite(jose, ana);
		event2.invite(jose, pedro);
		event2.invite(jose, pedro);
		event2.invite(ana, pedro);
		event2.invite(ana, pedro);
		event2.invite(pedro, ana);
		event2.invite(pedro, ana);
		
		assertEquals(2, event2.allResultingInvitees().length);
		assertContentsInAnyOrder(Arrays.asList(event2.allResultingInvitees()), ana, pedro);
	}
	
	@Test
	public void toHappenSinceTwoHoursAgo() throws Refusal {
		Event firstEvent = createEvent(ana, "D1", 11);
		Event secondEvent = createEvent(ana, "D2", 12);
		Event thirdEvent = createEvent(ana, "D3", 13);

		assertEquals(3, subject.toHappen(ana).size());
		assertSame(firstEvent, subject.toHappen(ana).get(0));
		assertSame(secondEvent, subject.toHappen(ana).get(1));
		assertSame(thirdEvent, subject.toHappen(ana).get(2));

		Clock.setForCurrentThread(TWO_HOURS + 12);
		assertEquals(2, subject.toHappen(ana).size());
		assertFalse(subject.toHappen(ana).contains(firstEvent));
		assertTrue(subject.toHappen(ana).contains(secondEvent));
		assertTrue(subject.toHappen(ana).contains(thirdEvent));
		
		Clock.setForCurrentThread(TWO_HOURS + 13);
		assertEquals(1, subject.toHappen(ana).size());
		assertTrue(subject.toHappen(ana).contains(thirdEvent));

		Clock.setForCurrentThread(TWO_HOURS + 14);
		assertTrue(subject.toHappen(ana).isEmpty());
	}
	
	
	@Test
	public void notInterested() throws Refusal {
		Event event = createEvent(ana, "Dinner at Joes", 1000, jose);
		assertEquals(1, subject.toHappen(jose).size());

		event.setNotInterested(jose);
		assertEquals(0, subject.toHappen(jose).size());
		assertEquals(1, subject.toHappen(ana).size());
	}

	
	@Test
	public void deletion() throws Refusal {
		Event event = createEvent(ana, "Dinner at Joes", 1000, jose);

		assertTrue(event.isEditableBy(ana));
		assertFalse(event.isEditableBy(jose));
		subject.delete(ana, event);
		assertEquals(0, subject.toHappen(ana).size());
		assertEquals(0, subject.toHappen(jose).size());
	}
	
	@Test
	public void eventId() throws Refusal {
		Event event1 = createEvent(ana, "D1", 11);
		Event event2 = createEvent(ana, "D2", 12);
		Event event3 = createEvent(ana, "D3", 13);
		assertTrue(0 < event2.getId());
		assertTrue(event1.getId() < event2.getId());
		assertTrue(event2.getId() < event3.getId());
	}
	
}
