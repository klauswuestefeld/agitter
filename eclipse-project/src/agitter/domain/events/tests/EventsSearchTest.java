package agitter.domain.events.tests;

import org.junit.Test;

public class EventsSearchTest extends EventsTestBase {

	@Test
	public void addNew() throws Exception {
		createEvent(ana, "Dinner at Joes", 1000);
		createEvent(ana, "Dinner at Moes", 1001);
		createEvent(ana, "Party at Joes", 1002);
		assertEquals(2, subject.search("joE").size());
	}
	
	
}
