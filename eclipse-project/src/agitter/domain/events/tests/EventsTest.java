package agitter.domain.events.tests;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.events.Event;
import agitter.domain.events.Occurrence;

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

//		subject.setDatetimes(ana, secondEvent, new long[]{14, 15, 16});
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
		event.addInvitee(work);
		assertContents(Arrays.asList(event.groupInvitees()),work);
		assertContents(Arrays.asList(event.invitees()));

		Group friends = contacts.createGroup( "friends" );
		event.addInvitee(friends);
		event.addInvitee(jose);
		assertContentsInAnyOrder(Arrays.asList(event.groupInvitees()),work,friends);
		assertContents(Arrays.asList(event.invitees()),jose);
		
		event.removeInvitee(work);
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

		assertTrue(subject.isEditableBy(ana, event));
		assertFalse(subject.isEditableBy(jose, event));
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
	
	@Test
	public void eventIdMigration() throws Refusal {
		Event event1 = createEvent(ana, "D1", 11);
		Event event2 = createEvent(ana, "D2", 12);
		Event event3 = createEvent(ana, "D3", 13);
		subject.setLastId(0);
		event1.setId(0);
		event2.setId(0);
		event3.setId(0);
		agitter.migrateSchemaIfNecessary();
		assertTrue(0 < event2.getId());
		assertTrue(event1.getId() < event2.getId());
		assertTrue(event2.getId() < event3.getId());
	}
	


	@Test
	public void newEventHasOneOccurrence() throws Refusal {
		Event party = createEvent(ana, "Party at home", 1000);
		assertEquals(1, party.datetimes().length);
	}
	
	
	@Test
	public void notInterestedOccurrence() throws Refusal {
		Event party = createEvent(ana, "Barbecue at home", 1000, jose);
		Clock.setForCurrentThread( 10 );
		
		Occurrence d2000 = occurrenceOn(party, 2000);
		assertEquals(2000, d2000.datetime());
		assertTrue(d2000.isInterested(ana));
		assertTrue(d2000.isInterested(jose));
		
		Occurrence d3000 = occurrenceOn(party, 3000);
		assertEquals(3000, d3000.datetime());
		assertTrue(d3000.isInterested(ana));
		assertTrue(d3000.isInterested(jose));
		assertEquals(3, party.occurrences().length);
	
		d2000.notInterested(jose);
		
		d2000 = searchOccurrence(party, 2000);
		assertTrue(d2000.isInterested(ana));
		assertTrue(!d2000.isInterested(jose));

		d3000 = searchOccurrence(party, 3000);
		assertTrue(d3000.isInterested(ana));
		assertTrue(d3000.isInterested(jose));

		Occurrence d1000 = searchOccurrence(party, 1000);
		assertTrue(d1000.isInterested(ana));
		assertTrue(d1000.isInterested(jose));
		
		party.notInterested(jose, 3000);
		assertTrue(d3000.isInterested(ana));
		assertTrue(!d3000.isInterested(jose));
		
		assertTrue(d1000.isInterested(ana));
		assertTrue(d1000.isInterested(jose));
		assertTrue(d2000.isInterested(ana));
		assertTrue(!d2000.isInterested(jose));
	}

	
	private Occurrence occurrenceOn(Event event, long datetime) {
		event.addDate(datetime);
		return searchOccurrence(event, datetime);
	}
	
	private Occurrence searchOccurrence(Event event, long datetime) {
		for (Occurrence occ : event.occurrences()) {
			if (occ.datetime() == datetime) return occ;
		}
		return null;
	}

}
