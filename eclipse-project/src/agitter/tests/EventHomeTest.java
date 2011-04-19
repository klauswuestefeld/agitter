package agitter.tests;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import agitter.Event;
import agitter.EventHomeImpl;
import agitter.util.ClockMock;

public class EventHomeTest extends Assert {

	private final ClockMock _clock = new ClockMock();
	private final EventHomeImpl _subject = new EventHomeImpl(_clock);


	@Test
	public void creation() throws Exception {
		long now = _clock.datetime();
		_subject.create("Test", now);
		assertEquals(1, _subject.all().size());
	}
	
	
	@Test
	public void testToHappen() throws Exception {
		Event a1 = _subject.create("D1", 1);
		Event a2 = _subject.create("D2", 2);
		Event a3 = _subject.create("D3", 3);

		Set<Event> all = _subject.toHappen();
		assertTrue(all.size()==3);
		assertTrue(all.contains(a1));
		assertTrue(all.contains(a2));
		assertTrue(all.contains(a3));

		_clock.setDatetime(1);
		Set<Event> atD1 = _subject.toHappen();
		assertTrue(atD1.size()==2);
		assertFalse(atD1.contains(a1));
		assertTrue(atD1.contains(a2));
		assertTrue(atD1.contains(a3));

		_clock.setDatetime(2);
		Set<Event> atD2 = _subject.toHappen();
		assertTrue(atD2.size()==1);
		assertTrue(atD2.contains(a3));

		_clock.setDatetime(3);
		assertTrue(_subject.toHappen().isEmpty());

		assertTrue(_subject.all().size()==3);

	}

}
