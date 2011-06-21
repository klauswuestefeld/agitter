package agitter.domain.events.tests;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.users.User;
import agitter.domain.users.UserImpl;

public class EventsTest extends EventsTestBase {

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
		Event event = _subject.create(_ana, "Dinner at Joes", 1000, new EmailAddress("jose@gmail.com"));

		User jose = new UserImpl("Jose", "jose@gmail.com", "123");
		assertEquals(1, _subject.toHappen(jose).size());

		event.notInterested(jose);
		assertEquals(0, _subject.toHappen(jose).size());

		assertEquals(1, _subject.toHappen(_ana).size());
	}

}
