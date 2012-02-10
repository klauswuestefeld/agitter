package utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;

public abstract class SecureRequest {

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
		return new Encoder().computeHmac("QualquerSenha764", input);
	}

	
	abstract protected String command();

	protected void addParamToUri(String name, String value) {
		params += params.isEmpty() ? "?" : "&";
		params += name + "=" + value;
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
			String value = keysAndValues[i++];
			result.put(key, new String[]{value});
		}
		return result;
	}
	
}
