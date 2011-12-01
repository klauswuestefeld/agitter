package utils;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sneer.foundation.lang.exceptions.Refusal;



public abstract class RestRequest {

	private static final String HMAC_SHA256 = "HmacSHA256";
	private String params = "";
	
	public static Map<String, String[]> map(String req) throws Refusal {
		String[] keysAndValues = req.split( "[&=]" );
		Map<String, String[]> result = new HashMap<String, String[]>();
		int i = 0;
		while (i < keysAndValues.length) {
			String key = keysAndValues[i++];
			String value = keysAndValues[i++];
			result.put(key, new String[]{value});
		}
		return result;
	}

	
	public String asSecureURI() {
		addParamToUri("code", securityCode());
		return asURI();
	}

	
	private String asURI() {
		return command() + params;
	}

	
	protected String securityCode() {
		String input = asURI();
		return computeHmac("QualquerSenha764", input);
	}

	
	abstract protected String command();

	protected void addParamToUri(String name, String value) {
		params += params.isEmpty() ? "?" : "&";
		params += name + "=" + value;
	}
	
	
	private String computeHmac(String privateKey, String input) {
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


	protected void validate(Map<String, String[]> params) throws Refusal {
		String[] code = params.get("code");
		if (code == null || code.length != 1 || !securityCode().equals(code[0]))
			throw new Refusal("Requisição inválida.");
	}
	
	
	public static void main(String[] args) {
		System.err.println( Arrays.toString( "signup?email=matias@sumersoft.com&code=12789A159BCED6CC42F7EB9D41FA415DB91FE8AA8DBD4CF8985C0664927A6906".split( "\\?" ) ) );
	}
	
	
}
