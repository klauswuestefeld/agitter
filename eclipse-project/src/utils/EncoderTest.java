package utils;

import org.junit.Assert;
import org.junit.Test;

public class EncoderTest extends Assert {

	@Test
	public void hmacShouldBeConsistent() {
		assertEquals(new Encoder().computeHmac("agitter", "abcdef"),new Encoder().computeHmac("agitter", "abcdef"));
	}
	
	@Test
	public void differentKeysShouldLeadToDifferentResults() {
		assertFalse(new Encoder().computeHmac("klaus", "abcdef").equals(new Encoder().computeHmac("vitor", "abcdef")));
	}
	
	@Test
	public void differentInputsShouldLeadToDifferentResults() {
		assertFalse(new Encoder().computeHmac("klaus", "ab").equals(new Encoder().computeHmac("klaus", "abc")));
	}
}
