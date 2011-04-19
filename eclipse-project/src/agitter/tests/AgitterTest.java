package agitter.tests;

import java.io.File;

import agitter.AgitterSystem;
import agitter.Events;
import org.junit.Assert;
import org.junit.Test;

public class AgitterTest extends Assert {

	@Test
	public void persistence() throws Exception {
		final File tmpFolder = new File("./repo");
		if(!tmpFolder.exists()) { tmpFolder.mkdir(); }

		AgitterSystem.open(tmpFolder);
		Events events = AgitterSystem.execution().events();
		events.create("Dinner at Joe's", 1234);
		AgitterSystem.close();

		AgitterSystem.open(tmpFolder);
		assertEquals(1, AgitterSystem.execution().events().all().size());
	}
	

}
