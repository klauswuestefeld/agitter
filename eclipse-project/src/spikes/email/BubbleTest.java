package spikes.email;

import static agitter.domain.emails.EmailAddress.mail;

import java.util.Arrays;
import java.util.Collections;

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
		User ana = agitter.users().signup("ana", mail("ana@email.com"), "abc123");
		Events events = agitter.events();
		events.create(ana, "dinner", 10, Collections.EMPTY_LIST, Arrays.asList(jose()));
		Event event = events.toHappen(ana).get(0);
		
		User jose = agitter.users().searchByEmail(mail("jose@email.com"));
		assertEquals(1, events.toHappen(jose).size());
		event.notInterested(jose); // jose is a bubble wrapper
		assertEquals(0, events.toHappen(jose).size());
		
		PrevaylerBootstrap.close();
	}

	
	private User jose() throws Refusal {
		return agitter.users().produce(mail("jose@email.com"));
	}
	
}
