package agitter.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sneer.foundation.lang.Clock;
import agitter.Event;
import agitter.Events;
import agitter.EventsImpl;

public class EventsTest extends Assert {

	private final Events _subject = new EventsImpl();

	@Test
	public void addNew() throws Exception {
		_subject.create("Dinner at Joes", 1000);
		assertEquals(1, _subject.all().size());
		Event event = _subject.all().iterator().next();
		assertEquals("Dinner at Joes", event.description());
		assertEquals(1000, event.datetime());
	}
	
	
	@Test
	public void toHappen() throws Exception {
		Event firstEvent = _subject.create("D1", 1);
		Event secondEvent = _subject.create("D2", 2);
		Event thirdEvent = _subject.create("D3", 3);

		assertTrue(_subject.toHappen().size()==3);
		assertTrue(_subject.toHappen().contains(firstEvent));
		assertTrue(_subject.toHappen().contains(secondEvent));
		assertTrue(_subject.toHappen().contains(thirdEvent));

		Clock.setForCurrentThread(1);
		assertTrue(_subject.toHappen().size()==2);
		assertFalse(_subject.toHappen().contains(firstEvent));
		assertTrue(_subject.toHappen().contains(secondEvent));
		assertTrue(_subject.toHappen().contains(thirdEvent));
		
		Clock.setForCurrentThread(2);
		assertTrue(_subject.toHappen().size()==1);
		assertTrue(_subject.toHappen().contains(thirdEvent));

		Clock.setForCurrentThread(3);
		assertTrue(_subject.toHappen().isEmpty());

		assertTrue(_subject.all().size()==3);
	}
	
	@Test
	public void remove() {
		Event eventToRemove = _subject.create("one event", 1);
		assertEquals(1, _subject.all().size());
		
		_subject.remove(eventToRemove);
		assertEquals(0, _subject.all().size());
	}

	
	@Before
	public void beforeEventsTest() {
		Clock.setForCurrentThread(0);
	}
	@After
	public void afterEventsTest() {
		Clock.clearForCurrentThread();
	}

}
