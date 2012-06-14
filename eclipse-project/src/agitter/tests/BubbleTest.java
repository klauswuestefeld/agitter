package agitter.tests;

import static agitter.domain.emails.EmailAddress.email;

import agitter.domain.events.EventOcurrence;
import org.junit.Test;

import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.main.PrevaylerBootstrap;
import basis.lang.exceptions.Refusal;
import basis.testsupport.CleanTestBase;

public class BubbleTest extends CleanTestBase {

	Agitter agitter;

	@Test
	public void wrappedObjectPassedAsArg() throws Exception {
		PrevaylerBootstrap.open(tmpFolder());
		agitter = PrevaylerBootstrap.agitter();
		User ana = agitter.users().signup(email("ana@email.com"), "abc123");
		Events events = agitter.events();
		events.create(ana, "dinner", 10);
		EventOcurrence eventOcurrence = events.toHappen(ana).get(0);
		Event event = eventOcurrence.event();
		event.invite(ana, jose());
		
		User jose = agitter.users().searchByEmail(email("jose@email.com"));
		assertEquals(1, events.toHappen(jose).size());
		event.setNotInterested(jose); // jose is a bubble wrapper
		assertEquals(0, events.toHappen(jose).size());
		
		PrevaylerBootstrap.close();
	}

	
	private User jose() throws Refusal {
		return agitter.users().produce(email("jose@email.com"));
	}
	
}
