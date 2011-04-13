package agitter.server.crypt;

import junit.framework.Assert;

import org.junit.Test;

public class CryptoTest {
	
	@Test
	public void testCrypt() throws CryptorException {
		String frase = "This is a secret";
		String encoded = new Cryptor().encode(frase);
		String decoded = new Cryptor().decode(encoded);
		Assert.assertEquals(frase, decoded);
	}
}
