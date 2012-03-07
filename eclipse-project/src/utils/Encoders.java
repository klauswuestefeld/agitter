package utils;

import java.nio.charset.Charset;
import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sneer.foundation.lang.Functor;

public class Encoders {

	private static final Charset UTF_8 = Charset.forName("UTF-8");
	private static final String HMAC_SHA256 = "HmacSHA256";

	
	public static Functor<String, byte[]> hmacForKey(String secretKey) {
		final Mac mac = macFor(secretKey);
		return new Functor<String, byte[]>() { @Override public byte[] evaluate(String input) {
			return mac.doFinal(input.getBytes(UTF_8));
		}};
	}

	
	private static Mac macFor(String secretKey) {
		byte[] keyBytes = secretKey.getBytes(UTF_8);
		Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, HMAC_SHA256);
		Mac mac = getAlgorithm(HMAC_SHA256, key);
		return mac;
	}

	
	static private Mac getAlgorithm(String algorithm, Key key) {
		try {
			Mac mac = Mac.getInstance(algorithm);
			mac.init(key);
			return mac;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	
	static public String toHex(byte[] bytes) {
		int hn, ln, cx;
		String hexDigitChars = "0123456789ABCDEF";
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for(cx = 0; cx < bytes.length; cx++) {
			hn = ((int)(bytes[cx]) & 0x00ff) / 16;
			ln = ((int)(bytes[cx]) & 0x000f);
			buf.append(hexDigitChars.charAt(hn));
			buf.append(hexDigitChars.charAt(ln));
		}
		return buf.toString();
	}

}
