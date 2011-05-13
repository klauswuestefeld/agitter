package agitter.ui.presenter.tests;

import org.junit.Test;

import sneer.foundation.testsupport.AssertUtils;
import agitter.ui.presenter.Credential;


public class CredentialTest extends AssertUtils {
	private static String SAMPLE_EMAIL = "user1@somewhere.com";
	private static String SAMPLE_USERNAME = "user1";
	private static String SAMPLE_PASSWORD = "pwd";
	
	@Test
	public void credentialToString(){
		assertEquals(SAMPLE_EMAIL, credentialWithEmailProvided().toString());
	}
	
	@Test
	public void credentialShouldHavePassword(){
		assertEquals(SAMPLE_PASSWORD, credentialWithEmailProvided().password());
	}

	@Test
	public void credentialShouldKnowHowToExtractUserNameFromEmail(){
		assertEquals(SAMPLE_USERNAME, credentialWithEmailProvided().suggestedUserName());
	}
	
	@Test
	public void emailShoudBeEmptyWhenUserNameIsProvided(){
		assertEquals(SAMPLE_USERNAME, credentialWithJustUserNameProvided().username());
		assertEquals("", credentialWithJustUserNameProvided().email());
	}
	
	@Test
	public void shouldKnowIfEmailWasProvided(){
		assertEquals(false, credentialWithJustUserNameProvided().isEmailProvided());
		assertEquals(true, credentialWithEmailProvided().isEmailProvided());
	}
	
	private Credential credentialWithEmailProvided(){
		return new Credential(SAMPLE_EMAIL, SAMPLE_PASSWORD);
	}
	private Credential credentialWithJustUserNameProvided(){
		return new Credential(SAMPLE_USERNAME, SAMPLE_PASSWORD);
	}
}
