package utils;

import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Encoder {

	private static final String HMAC_SHA256 = "HmacSHA256";

	static final String PRIVATE_KEY_FOR_PASSWORD = "agitter#pk$skype@123*bacalhau+9090";
	
	public String encryptPassword(String rawPassword) {
		return rawPassword != null ? 
				this.computeHmac(PRIVATE_KEY_FOR_PASSWORD, rawPassword): 
				null;
	}
	
	public String computeHmac(String privateKey, String input) {
		byte[] keyBytes = privateKey.getBytes();
		Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, HMAC_SHA256);
		Mac mac = getAlgorithm(HMAC_SHA256, key);
		return toHex(mac.doFinal(input.getBytes()));
	}

	private Mac getAlgorithm(String algorithm, Key key) {
		try {
			Mac mac = Mac.getInstance(algorithm);
			mac.init(key);
			return mac;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	private String toHex(byte [] a) {
		int hn, ln, cx;
		String hexDigitChars = "0123456789ABCDEF";
		StringBuffer buf = new StringBuffer(a.length * 2);
		for(cx = 0; cx < a.length; cx++) {
			hn = ((int)(a[cx]) & 0x00ff) / 16;
			ln = ((int)(a[cx]) & 0x000f);
			buf.append(hexDigitChars.charAt(hn));
			buf.append(hexDigitChars.charAt(ln));
		}
		return buf.toString();
	}

}
