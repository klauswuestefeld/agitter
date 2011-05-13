package agitter.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.users.Credential;
import agitter.main.PrevaylerBootstrap;

public class PersistenceTest extends CleanTestBase {

	@Test
	public void persistence() throws Refusal {
		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().users().signup("ana", "ana@gmail.com", "ana123");
		PrevaylerBootstrap.close();

		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().users().login(new Credential("ana@gmail.com", "ana123"));

		PrevaylerBootstrap.close();
	}
	

}
