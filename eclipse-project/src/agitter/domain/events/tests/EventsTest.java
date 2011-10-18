package agitter.domain.events.tests;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Event;

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
	public void ids() throws Exception {
		Event e1 = createEvent(ana, "Dinner at Joes", 1000);
		Event e2 = createEvent(ana, "Dinner at Klaus", 1002);

		assertSame(e1, subject.searchById(e1.id()));
		assertSame(e2, subject.searchById(e2.id()));
	}

	@Test
	public void changingEventTime() throws Refusal {
		Event firstEvent = createEvent(ana, "D1", 11);
		Event secondEvent = createEvent(ana, "D2", 12);
		Event thirdEvent = createEvent(ana, "D3", 13);

		subject.setEventTime(secondEvent, 14);

		assertEquals(3, subject.toHappen(ana).size());
		assertSame(firstEvent, subject.toHappen(ana).get(0));
		assertSame(thirdEvent, subject.toHappen(ana).get(1));
		assertSame(secondEvent, subject.toHappen(ana).get(2));		

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

		assertTrue(subject.isDeletableBy(event, ana));
		assertFalse(subject.isDeletableBy(event, jose));
		subject.delete(event, ana);
		assertEquals(0, subject.toHappen(ana).size());
		assertEquals(0, subject.toHappen(jose).size());
	}

}
