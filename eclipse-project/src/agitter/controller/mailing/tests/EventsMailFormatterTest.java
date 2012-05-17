package agitter.controller.mailing.tests;

import static java.util.Arrays.asList;

import agitter.domain.events.EventOcurrence;
import agitter.domain.events.EventOcurrenceImpl;
import org.junit.Test;

import agitter.controller.mailing.EventsMailFormatter;
import agitter.domain.events.tests.EventsTestBase;

public class EventsMailFormatterTest extends EventsTestBase {

	private final EventsMailFormatter subject = new EventsMailFormatter();
	

	@Test
	public void mailHasAuthenticationLink() throws Exception {
		EventOcurrence eventOcurrence = new EventOcurrenceImpl(createEvent(jose, "party", 42), 42);
		String body = subject.format(ana, asList(eventOcurrence));
		String link = "<a href=\"http://agitter.com/auth?expires=172800001&email=ana%40email.com&code=";
		assertTrue("Mail body should contain '" + link + "':\n" + body, body.contains(link));
	}

}
