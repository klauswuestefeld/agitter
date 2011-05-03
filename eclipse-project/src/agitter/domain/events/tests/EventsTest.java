package agitter.domain.events.tests;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.User;
import agitter.domain.UserImpl;
import agitter.domain.events.PublicEvent;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;

public class EventsTest extends CleanTestBase {

	private final Events _subject = new EventsImpl();
	private final User _ana = new UserImpl("Ana", "ana@gmail.com", "123x");
	
	@Test
	public void addNew() throws Exception {
		_subject.create(_ana, "Dinner at Joes", 1000);
		assertEquals(1, _subject.toHappen(_ana).size());
		PublicEvent publicEvent = _subject.toHappen(_ana).iterator().next();
		assertEquals("Dinner at Joes", publicEvent.description());
		assertEquals(1000, publicEvent.datetime());
	}
	
	
	@Test
	public void toHappen() throws Refusal {
		PublicEvent firstPublicEvent = _subject.create(_ana, "D1", 11);
		PublicEvent secondPublicEvent = _subject.create(_ana, "D2", 12);
		PublicEvent thirdPublicEvent = _subject.create(_ana, "D3", 13);

		assertTrue(_subject.toHappen(_ana).size()==3);
		assertTrue(_subject.toHappen(_ana).contains(firstPublicEvent));
		assertTrue(_subject.toHappen(_ana).contains(secondPublicEvent));
		assertTrue(_subject.toHappen(_ana).contains(thirdPublicEvent));

		Clock.setForCurrentThread(11);
		assertTrue(_subject.toHappen(_ana).size()==2);
		assertFalse(_subject.toHappen(_ana).contains(firstPublicEvent));
		assertTrue(_subject.toHappen(_ana).contains(secondPublicEvent));
		assertTrue(_subject.toHappen(_ana).contains(thirdPublicEvent));
		
		Clock.setForCurrentThread(12);
		assertTrue(_subject.toHappen(_ana).size()==1);
		assertTrue(_subject.toHappen(_ana).contains(thirdPublicEvent));

		Clock.setForCurrentThread(13);
		assertTrue(_subject.toHappen(_ana).isEmpty());


	}
	
	
	@Test
	public void notInterested() throws Refusal {
		PublicEvent publicEventToRemove = _subject.create(_ana, "Dinner at Joes", 1000);
		assertEquals(1, _subject.toHappen(_ana).size());

		publicEventToRemove.notInterested(_ana);		
		assertEquals(0, _subject.toHappen(_ana).size());

		User jose = new UserImpl("Jose", "jose@gmail.com", "123");
		assertEquals(1, _subject.toHappen(jose).size());

	}

}
