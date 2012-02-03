package utils;

import org.junit.Assert;
import org.junit.Test;

public class XssAttackSanitizerTest extends Assert {

	@Test
	public void ultraConservativeFilter() {
		String result = XssAttackSanitizer.ultraConservativeFilter("\\//B a\rn\ta\nn a@#&*()+='\"[]{}´`~^çÇãáÂÊ;%$.,-_><");
		assertEquals("//B a n a n a@çÇãáÂÊ.,-_", result);
	}
	
	@Test
	public void failMethod() {
		fail();
	}
}
