package agitter.tests;

import org.junit.Ignore;
import org.junit.Test;

import sneer.foundation.testsupport.CleanTestBase;
import agitter.Events;
import agitter.util.PrevaylerBootstrap;

@Ignore
public class AgitterTest extends CleanTestBase {

	@Test
	public void persistence() throws Exception {
		PrevaylerBootstrap.open(tmpFolder());
		Events events = PrevaylerBootstrap.execution().events();
		events.create("Dinner at Joe's", 1234);
		PrevaylerBootstrap.close();

		PrevaylerBootstrap.open(tmpFolder());
		assertEquals(1, PrevaylerBootstrap.execution().events().all().size());
	}
	

}
