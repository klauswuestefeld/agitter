package agitter.tests;

import static agitter.domain.emails.EmailAddress.email;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.main.PrevaylerBootstrap;

public class BubbleTest extends CleanTestBase {

	Agitter agitter;

	@Test
	public void wrappedObjectPassedAsArg() throws Exception {
		PrevaylerBootstrap.open(tmpFolder());
		agitter = PrevaylerBootstrap.agitter();
		User ana = agitter.users().signup(email("ana@email.com"), "abc123");
		Events events = agitter.events();
		events.create(ana, "dinner", 10);
		Event event = events.toHappen(ana).get(0);
		event.invite(ana, jose());
		
		User jose = agitter.users().searchByEmail(email("jose@email.com"));
		assertEquals(1, events.toHappen(jose).size());
		event.notInterested(jose); // jose is a bubble wrapper
		assertEquals(0, events.toHappen(jose).size());
		
		PrevaylerBootstrap.close();
	}

	
	private User jose() throws Refusal {
		return agitter.users().produce(email("jose@email.com"));
	}
	
}
