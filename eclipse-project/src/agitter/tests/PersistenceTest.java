package agitter.tests;

import static agitter.domain.emails.EmailAddress.email;

import org.junit.Test;

import basis.testsupport.CleanTestBase;

import agitter.main.PrevaylerBootstrap;

public class PersistenceTest extends CleanTestBase {

	@Test
	public void persistence() throws Exception {
		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().users().signup(email("ana@gmail.com"), "ana123");
		PrevaylerBootstrap.close();

		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().users().loginWithEmail(email("ana@gmail.com"), "ana123");

		PrevaylerBootstrap.close();
	}
	

}
