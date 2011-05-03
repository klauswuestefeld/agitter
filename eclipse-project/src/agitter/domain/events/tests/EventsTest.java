package agitter.domain.events.tests;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.User;
import agitter.domain.UserImpl;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;

public class EventsTest extends CleanTestBase {

	private final Events _subject = new EventsImpl();
	private final User _ana = new UserImpl("Ana", "ana@gmail.com", "123x");
	
	@Test
	public void addNew() throws Exception {
		_subject.create(_ana, "Dinner at Joes", 1000);
		assertEquals(1, _subject.toHappen(_ana).size());
		Event event = _subject.toHappen(_ana).iterator().next();
		assertEquals("Dinner at Joes", event.description());
		assertEquals(1000, event.datetime());
	}
	
	
	@Test
	public void toHappen() throws Refusal {
		Event firstEvent = _subject.create(_ana, "D1", 11);
		Event secondEvent = _subject.create(_ana, "D2", 12);
		Event thirdEvent = _subject.create(_ana, "D3", 13);

		assertTrue(_subject.toHappen(_ana).size()==3);
		assertTrue(_subject.toHappen(_ana).contains(firstEvent));
		assertTrue(_subject.toHappen(_ana).contains(secondEvent));
		assertTrue(_subject.toHappen(_ana).contains(thirdEvent));

		Clock.setForCurrentThread(11);
		assertTrue(_subject.toHappen(_ana).size()==2);
		assertFalse(_subject.toHappen(_ana).contains(firstEvent));
		assertTrue(_subject.toHappen(_ana).contains(secondEvent));
		assertTrue(_subject.toHappen(_ana).contains(thirdEvent));
		
		Clock.setForCurrentThread(12);
		assertTrue(_subject.toHappen(_ana).size()==1);
		assertTrue(_subject.toHappen(_ana).contains(thirdEvent));

		Clock.setForCurrentThread(13);
		assertTrue(_subject.toHappen(_ana).isEmpty());
	}
	
	
	@Test
	public void remove() throws Refusal {
		Event eventToRemove = _subject.create(_ana, "Dinner at Joes", 1000);
		assertEquals(1, _subject.toHappen(_ana).size());
		
		_subject.remove(_ana, eventToRemove);
		assertEquals(0, _subject.toHappen(_ana).size());
	}

}
