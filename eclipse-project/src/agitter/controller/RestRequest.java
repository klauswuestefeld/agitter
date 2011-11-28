package agitter.controller;
import java.security.Key;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sneer.foundation.lang.exceptions.Refusal;



public abstract class RestRequest {

	private static final String HMAC_SHA256 = "HmacSHA256";
	private String params = "";

	
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
	
}
