package agitter.tests;

import static agitter.domain.emails.EmailAddress.mail;

import agitter.domain.users.User;
import org.junit.Test;

import sneer.foundation.testsupport.CleanTestBase;
import agitter.main.PrevaylerBootstrap;

public class PersistenceTest extends CleanTestBase {

	@Test
	public void persistence() throws Exception {
		PrevaylerBootstrap.open(tmpFolder());
		User ana = PrevaylerBootstrap.agitter().users().signup(mail("ana@gmail.com"), "ana123");
		ana.activate();
		PrevaylerBootstrap.close();

		PrevaylerBootstrap.open(tmpFolder());
		PrevaylerBootstrap.agitter().users().loginWithEmail(mail("ana@gmail.com"), "ana123");

		PrevaylerBootstrap.close();
	}
	

}
