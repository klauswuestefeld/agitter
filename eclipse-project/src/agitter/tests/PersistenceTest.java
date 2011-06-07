package agitter.tests;

import org.junit.Test;

import sneer.foundation.testsupport.CleanTestBase;
import agitter.main.PrevaylerBootstrap;

public class PersistenceTest extends CleanTestBase {

	@Test
	public void persistence() throws Exception {
		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().users().signup("ana", "ana@gmail.com", "ana123");
		PrevaylerBootstrap.close();

		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().users().loginWithEmail("ana@gmail.com", "ana123");

		PrevaylerBootstrap.close();
	}
	

}
