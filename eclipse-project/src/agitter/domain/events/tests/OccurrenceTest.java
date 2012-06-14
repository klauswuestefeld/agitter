package agitter.domain.events.tests;

import static agitter.domain.events.Event.Attendance.GOING;
import static agitter.domain.events.Event.Attendance.MAYBE;
import static agitter.domain.events.Event.Attendance.NOT_GOING;

import org.junit.Test;

import basis.lang.exceptions.Refusal;

import agitter.domain.events.Event;

public class OccurrenceTest extends EventsTestBase {

	@Test
	public void newEventHasOneOccurrence() throws Refusal {
		Event party = createEvent(ana, "Party at home", 1000);
		assertEquals(1, party.datetimes().length);
	}
	
	
	@Test
	public void testDecisions() throws Refusal {
		Event party = createEvent(ana, "Party at home", 1000);
		
		assertEquals(GOING, party.attendance(ana, 1000));
		assertNull(party.attendance(jose, 1000));
		
		party.setAttendance(jose, 1000, GOING);
		assertEquals(GOING, party.attendance(jose, 1000));
		
		party.setAttendance(ana, 1000, MAYBE);
		assertEquals(MAYBE, party.attendance(ana, 1000));

		party.setAttendance(jose, 1000, NOT_GOING);
		assertEquals(NOT_GOING, party.attendance(jose, 1000));
	}
	
}
