package agitter.domain.users.tests;

import org.junit.Test;

import agitter.domain.users.Credential;
import sneer.foundation.testsupport.AssertUtils;


public class CredentialTest extends AssertUtils {
	private static String SAMPLE_EMAIL = "user1@somewhere.com";
	private static String SAMPLE_USERNAME = "user1";
	private static String SAMPLE_PASSWORD = "pwd";
	
	@Test
	public void credentialShouldHaveIdAndPassword(){
		assertEquals(SAMPLE_EMAIL, credentialWithEmailProvided().getId());
		assertEquals(SAMPLE_PASSWORD, credentialWithEmailProvided().getPassword());
	}
	
	@Test
	public void credentialShouldKnowHowToExtractUserNameFromEmail(){
		assertEquals(SAMPLE_USERNAME, credentialWithEmailProvided().getUserName());
	}
	
	@Test
	public void emailShoudBeEmptyWhenUserNameIsProvided(){
		assertEquals(SAMPLE_USERNAME, credentialWithJustUserNameProvided().getUserName());
		assertEquals("", credentialWithJustUserNameProvided().getEmail());
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
