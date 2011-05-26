package spikes.email;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.main.PrevaylerBootstrap;

public class BubbleTest extends CleanTestBase {

	@Test
	public void wrappedObjectPassedAsArg() throws Refusal {
		PrevaylerBootstrap.open(tmpFolder());
		Agitter agitter = PrevaylerBootstrap.agitter();
		User ana = agitter.users().signup("ana", "ana@email.com", "abc123");
		Events events = agitter.events();
		events.create(ana, "dinner", 10);
		Event event = events.toHappen(ana).get(0);
		
		event.notInterested(ana); // ana is a bubble wrapper
		assertEquals(0, events.toHappen(ana).size());
		
		PrevaylerBootstrap.close();
	}
	
}
