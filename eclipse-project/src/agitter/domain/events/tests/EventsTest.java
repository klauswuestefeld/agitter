package agitter.domain.events.tests;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.events.Event;
import agitter.domain.users.User;

public class EventsTest extends EventsTestBase {

	private static final long TWO_HOURS = 1000 * 60 * 60 * 2;

	
	@Test
	public void addNew() throws Exception {
		createEvent(ana, "Dinner at Joes", 1000);
		assertEquals(1, subject.toHappen(ana).size());
		Event event = subject.toHappen(ana).iterator().next();
		assertEquals("Dinner at Joes", event.description());
		assertEquals(1000, event.datetime());
	}
	

	@Test
	public void changingEventTime() throws Refusal {
		Event firstEvent = createEvent(ana, "D1", 11);
		Event secondEvent = createEvent(ana, "D2", 12);
		Event thirdEvent = createEvent(ana, "D3", 13);

		subject.edit(ana, secondEvent, "D2", 14, Collections.EMPTY_LIST, Collections.EMPTY_LIST);

		assertEquals(3, subject.toHappen(ana).size());
		assertSame(firstEvent, subject.toHappen(ana).get(0));
		assertSame(thirdEvent, subject.toHappen(ana).get(1));
		assertSame(secondEvent, subject.toHappen(ana).get(2));		

	}


	@Test
	public void createAndEditEventWithGroups() throws Refusal {
		ContactsOfAUser contacts = agitter.contacts().contactsOf( ana );
		Group work = contacts.createGroup( "work" );
		
		Event event =  subject.create(ana, "Churras", 12, Arrays.asList( new Group[]{work}), Collections.EMPTY_LIST );
		assertContents(Arrays.asList(event.groupInvitees()),work);
		assertContents(Arrays.asList(event.invitees()));

		Group friends = contacts.createGroup( "friends" );
		subject.edit(ana, event, "Churras", 12, Arrays.asList( new Group[]{work,friends}), Arrays.asList(new User[]{jose}));
		assertContentsInAnyOrder(Arrays.asList(event.groupInvitees()),work,friends);
		assertContents(Arrays.asList(event.invitees()),jose);
		
		subject.edit(ana, event, "Churras", 12, Arrays.asList( new Group[]{friends}), Arrays.asList(new User[]{jose}));
		assertContentsInAnyOrder(Arrays.asList(event.groupInvitees()),friends);
		assertContents(Arrays.asList(event.invitees()),jose);
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

		event.notInterested(jose);
		assertEquals(0, subject.toHappen(jose).size());
		assertEquals(1, subject.toHappen(ana).size());
	}

	
	@Test
	public void deletion() throws Refusal {
		Event event = createEvent(ana, "Dinner at Joes", 1000, jose);

		assertTrue(subject.isEditableBy(event, ana));
		assertFalse(subject.isEditableBy(event, jose));
		subject.delete(event, ana);
		assertEquals(0, subject.toHappen(ana).size());
		assertEquals(0, subject.toHappen(jose).size());
	}

}
