package agitter.domain.events.tests;

import agitter.domain.events.Event;
import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;
import agitter.domain.users.User;
import agitter.domain.users.UserImpl;

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

		Clock.setForCurrentThread(11);
		assertEquals(3, _subject.toHappen(_ana).size());
		assertTrue(_subject.toHappen(_ana).contains(firstEvent));
		assertTrue(_subject.toHappen(_ana).contains(secondEvent));
		assertTrue(_subject.toHappen(_ana).contains(thirdEvent));

		Clock.setForCurrentThread(12);
		assertEquals(2, _subject.toHappen(_ana).size());
		assertFalse(_subject.toHappen(_ana).contains(firstEvent));
		assertTrue(_subject.toHappen(_ana).contains(secondEvent));
		assertTrue(_subject.toHappen(_ana).contains(thirdEvent));
		
		Clock.setForCurrentThread(13);
		assertEquals(1, _subject.toHappen(_ana).size());
		assertTrue(_subject.toHappen(_ana).contains(thirdEvent));

		Clock.setForCurrentThread(14);
		assertTrue(_subject.toHappen(_ana).isEmpty());


	}
	
	
	@Test
	public void notInterested() throws Refusal {
		Event eventToRemove = _subject.create(_ana, "Dinner at Joes", 1000);
		assertEquals(1, _subject.toHappen(_ana).size());

		eventToRemove.notInterested(_ana);
		assertEquals(0, _subject.toHappen(_ana).size());

		User jose = new UserImpl("Jose", "jose@gmail.com", "123");
		assertEquals(1, _subject.toHappen(jose).size());
	}

}
