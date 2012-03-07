package utils;

import java.util.Arrays;

import org.junit.Test;

import sneer.foundation.lang.Functor;
import sneer.foundation.testsupport.CleanTestBase;

public class EncoderTest extends CleanTestBase {

	Functor<String, byte[]> hmac1 = Encoders.hmacForKey("111");
	Functor<String, byte[]> hmac1b = Encoders.hmacForKey("111");
	Functor<String, byte[]> hmac2 = Encoders.hmacForKey("222");

	@Test
	public void hmacShouldBeConsistent() {
		assertArrayEquals(hmac1.evaluate("abc"), hmac1.evaluate("abc"));
		assertArrayEquals(hmac1.evaluate("abc"), hmac1b.evaluate("abc"));
	}
	
	
	@Test
	public void differentKeysShouldLeadToDifferentResults() {
		assertFalse(Arrays.equals(hmac1.evaluate("abc"), hmac2.evaluate("abc")));
	}
	
	
	@Test
	public void differentInputsShouldLeadToDifferentResults() {
		assertFalse(Arrays.equals(hmac1.evaluate("abc"), hmac1.evaluate("def")));
	}
}
