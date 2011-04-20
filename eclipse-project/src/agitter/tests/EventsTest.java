package agitter.tests;

import org.junit.Assert;
import org.junit.Test;

import agitter.Event;
import agitter.Events;
import agitter.EventsImpl;
import agitter.util.AgitterClockMock;

public class EventsTest extends Assert {

	private final AgitterClockMock _clock = new AgitterClockMock();
	private final Events _events = new EventsImpl(_clock);


	@Test
	public void addNew() throws Exception {
		long now = _clock.datetime();
		_events.create("a new event", now);
		assertEquals(1, _events.all().size());
	}
	
	
	@Test
	public void toHappen() throws Exception {
		Event firstEvent = _events.create("D1", 1);
		Event secondEvent = _events.create("D2", 2);
		Event thirdEvent = _events.create("D3", 3);

		assertTrue(_events.toHappen().size()==3);
		assertTrue(_events.toHappen().contains(firstEvent));
		assertTrue(_events.toHappen().contains(secondEvent));
		assertTrue(_events.toHappen().contains(thirdEvent));

		_clock.setDatetime(1);
		assertTrue(_events.toHappen().size()==2);
		assertFalse(_events.toHappen().contains(firstEvent));
		assertTrue(_events.toHappen().contains(secondEvent));
		assertTrue(_events.toHappen().contains(thirdEvent));
		
		_clock.setDatetime(2);
		assertTrue(_events.toHappen().size()==1);
		assertTrue(_events.toHappen().contains(thirdEvent));

		_clock.setDatetime(3);
		assertTrue(_events.toHappen().isEmpty());

		assertTrue(_events.all().size()==3);

	}
	
	@Test
	public void remove() {
		Event eventToRemove = _events.create("one event", 1);
		assertEquals(1, _events.all().size());
		
		_events.remove(eventToRemove);
		assertEquals(0, _events.all().size());
	}

}
