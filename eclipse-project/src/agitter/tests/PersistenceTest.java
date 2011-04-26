package agitter.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.main.PrevaylerBootstrap;

public class PersistenceTest extends CleanTestBase {

	@Test
	public void persistence() throws Refusal {
		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().signup("Ana", "ana@gmail.com", "ana123");
		PrevaylerBootstrap.close();

		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().login("ana@gmail.com", "ana123");
	}
	

}
