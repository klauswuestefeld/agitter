package utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;

public abstract class SecureRequest {

	private static final Functor<String, byte[]> HMAC = Encoders.hmacForKey("QualquerSenHa764");
	
	private final Map<String, String> params = new HashMap<String, String>();
	
	public String asSecureURI() {
		addParamToUri("code", securityCode());
		return asURI(true);
	}
	
	private static String encodeUrlValue(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("Invalid encoding");
		}
	}

	private static String decodeUrlValue(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("Invalid encoding");
		}
	}

	private String asURI(boolean encode) {
		String paramsString = "";
		for(Map.Entry<String, String> entry : params.entrySet()) {
			String key = encode ? encodeUrlValue(entry.getKey()) : entry.getKey();
			String value = encode ? encodeUrlValue(entry.getValue()) : entry.getValue();
			if(paramsString.isEmpty())
				paramsString += "?";
			else 
				paramsString += "&";
			paramsString += key + "=" + value;
		}
		return command() + paramsString;
	}
	
	protected String securityCode() {
		String input = asURI(false);
		return Encoders.toHex(HMAC.evaluate(input));
	}

	
	abstract protected String command();

	protected void addParamToUri(String name, String value) {
		params.put(name, value);
	}

	protected void validate(Map<String, String[]> params) throws Refusal {
		String[] code = params.get("code");
		if (code == null || code.length != 1 || !securityCode().equals(code[0]))
			throw new Refusal("Requisição inválida.");
	}
	
	
	public static void main(String[] args) {
		System.err.println( Arrays.toString( "signup?email=matias@sumersoft.com&code=12789A159BCED6CC42F7EB9D41FA415DB91FE8AA8DBD4CF8985C0664927A6906".split( "\\?" ) ) );
	}

	
	protected static Map<String, String[]> parseUri(String command, String uri) throws Refusal {
		String[] parts = uri.split("\\?");
		if(parts.length != 2)
			throw new Refusal( "Invalid " + command + " request: " + uri);
		if(!parts[0].equals(command))
			throw new Refusal( "Invalid request. Expected: " + command + " was: " + parts[0]);
	
		String[] keysAndValues = parts[1].split( "[&=]" );

		Map<String, String[]> result = new HashMap<String, String[]>();
		int i = 0;
		while (i < keysAndValues.length) {
			String key = keysAndValues[i++];
			String value = decodeUrlValue(keysAndValues[i++]);
			result.put(key, new String[]{value});
		}
		return result;
	}
	
}
