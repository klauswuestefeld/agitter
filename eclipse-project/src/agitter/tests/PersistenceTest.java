package agitter.tests;

import org.junit.Test;

import sneer.foundation.testsupport.CleanTestBase;
import agitter.Events;
import agitter.main.PrevaylerBootstrap;

public class PersistenceTest extends CleanTestBase {

	@Test
	public void persistence() throws Exception {
		PrevaylerBootstrap.open(tmpFolder());
		Events events = PrevaylerBootstrap.agitter().events();
		events.create("Dinner at Joe's", 1234);
		PrevaylerBootstrap.close();

		PrevaylerBootstrap.open(tmpFolder());
		assertEquals(1, PrevaylerBootstrap.agitter().events().all().size());
	}
	

}
