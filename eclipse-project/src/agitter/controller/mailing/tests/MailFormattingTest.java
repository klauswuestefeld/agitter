package agitter.controller.mailing.tests;

import static java.util.Arrays.asList;

import org.junit.Test;

import agitter.controller.mailing.InvitationMailFormatter;
import agitter.controller.mailing.ReminderMailFormatter;
import agitter.domain.events.Event;
import agitter.domain.events.EventOcurrence;
import agitter.domain.events.EventOcurrenceImpl;
import agitter.domain.events.tests.EventsTestBase;

public class MailFormattingTest extends EventsTestBase {

	@Test
	public void reminderHasAuthenticationLink() throws Exception {
		EventOcurrence eventOcurrence = new EventOcurrenceImpl(createEvent(jose, "party", 42), 42);
		String body = new ReminderMailFormatter().bodyFor(ana, asList(eventOcurrence));
		hasAuthLink(body);
	}

	
	@Test
	public void invitationHasAuthenticationLink() throws Exception {
		Event event = createEvent(jose, "party", 42);
		String body = new InvitationMailFormatter().bodyFor(ana, event);
		hasAuthLink(body);
	}


	private void hasAuthLink(String body) {
		String link = "<a href=\"http://agitter.com/auth?expires=172800001&email=ana%40email.com&code=";
		assertTrue("Mail body should contain '" + link + "':\n" + body, body.contains(link));
	}

}
