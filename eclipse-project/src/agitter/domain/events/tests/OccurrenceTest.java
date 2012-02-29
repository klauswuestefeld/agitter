package agitter.domain.events.tests;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Event;
import agitter.domain.events.Occurrence;

public class OccurrenceTest extends EventsTestBase {

	@Test
	public void newEventHasOneOccurrence() throws Refusal {
		Event party = createEvent(ana, "Party at home", 1000);
		assertEquals(1, party.datetimes().length);
	}
	
	@Test
	public void onlyInvitedCanNotInterestAOccurrence() throws Refusal {
		Event party = createEvent(ana, "Barbecue at home", 1000, jose);
		try { 
			party.notInterested(paulo);
			fail("Not supposed to allow Paulo remove interest in the party because he is not invited. ");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}
		
		try { 
			party.notInterested(paulo, 1000);
			fail("Not supposed to allow Paulo remove interest in the party because he is not invited. ");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}
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

	@Test
	public void testDecisions() throws Refusal {
		Event party = createEvent(ana, "Party at home", 1000);
		Occurrence date = searchOccurrence(party, 1000);
		
		assertTrue(date.isGoing(ana));
		assertNull(date.isGoing(jose));
		
		date.going(ana);
		date.going(jose);
		
		assertTrue(date.isGoing(ana));
		assertTrue(date.isGoing(jose));
		
		assertTrue(date.isInterested(ana));
		assertTrue(date.isInterested(jose));
		
		date.mayGo(ana);
		assertNull(date.isGoing(ana));
		
		date.notGoing(jose);
		assertFalse(date.isGoing(jose));
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
