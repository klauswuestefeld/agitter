package utils;

import java.util.Map;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public class AuthenticationToken extends SecureRequest {

	private static final String COMMAND = "auth";
	
	private final EmailAddress email;
	
	
	public AuthenticationToken(String uri) throws Refusal {
		this(SecureRequest.parseParams(COMMAND, uri));
	}
	
	public AuthenticationToken(Map<String, String[]> params) throws Refusal {
		this( email(params), validExpirationDate(params) );
		this.validate( params );
	}

	public AuthenticationToken(EmailAddress email) {
		this(email, -1);
	}
	public AuthenticationToken(EmailAddress email, long expirationDate) {
		this.email = email;
		addParamToUri("email", email.toString());
		addParamToUri("expires", String.valueOf(expirationDate));
	}

	private static EmailAddress email(Map<String, String[]> params) throws Refusal {
		return EmailAddress.email(params.get("email")[0]);
	}
	private static long validExpirationDate(Map<String, String[]> params) throws Refusal {
		long ret;
		try {
			ret = Long.parseLong(params.get("expires")[0]);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new Refusal("Autenticação com data inválida." );
		}
		if (ret == -1) return ret;
		if (ret >= Clock.currentTimeMillis()) return ret;
		throw new Refusal("Autenticação expirada. Faça o login.");
	}
	

	public EmailAddress email() {
		return email;
	}


	@Override
	protected String command() {
		return COMMAND;
	}

}
