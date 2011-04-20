package agitter.tests;

import java.io.File;

import agitter.Events;
import agitter.util.PrevaylerBootstrap;

import org.junit.Assert;
import org.junit.Test;

public class AgitterTest extends Assert {

	@Test
	public void persistence() throws Exception {
		final File tmpFolder = new File("./repo");
		if(!tmpFolder.exists()) { tmpFolder.mkdir(); }

		PrevaylerBootstrap.open(tmpFolder);
		Events events = PrevaylerBootstrap.execution().events();
		events.create("Dinner at Joe's", 1234);
		PrevaylerBootstrap.close();

		PrevaylerBootstrap.open(tmpFolder);
		assertEquals(1, PrevaylerBootstrap.execution().events().all().size());
	}
	

}
