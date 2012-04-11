
package agitter.ui.presenter.tests;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.controller.AuthenticationToken;
import agitter.domain.emails.EmailAddress;

public class AuthenticationTokenTest extends CleanTestBase {

	@Test
	public void encodeDecode() throws Refusal {
		EmailAddress address = EmailAddress.certain("matias.g.rodriguez@gmail.com");
		String secureURI = new AuthenticationToken(address).asSecureURI();
		
		assertEquals(address, new AuthenticationToken(secureURI).email()) ;
	}
	
}
