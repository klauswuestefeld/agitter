package agitter.controller.tests;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.controller.AuthenticationToken;
import agitter.domain.emails.EmailAddress;


public class AuthenticationTokenTest extends CleanTestBase {

	@Test(expected = Refusal.class)
	public void tokenExpires() throws Refusal {
		long expirationDate = 10;
		EmailAddress mail = EmailAddress.certain("a@a.com");
		String uri = new AuthenticationToken(mail, expirationDate).asSecureURI();
		
		Clock.setForCurrentThread(11);
		new AuthenticationToken(uri);
	}
	
}
