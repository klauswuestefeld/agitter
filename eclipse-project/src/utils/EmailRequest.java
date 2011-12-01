package utils;

import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public abstract class EmailRequest extends SecureRequest {

	private final EmailAddress email;
	
	
	public EmailRequest(Map<String, String[]> params) throws Refusal {
		this( email(params) );
		this.validate( params );
	}

	public EmailRequest(EmailAddress email) {
		this.email = email;
		addParamToUri("email", email.toString());
	}

	private static EmailAddress email(Map<String, String[]> params) throws Refusal {
		return EmailAddress.email(params.get("email")[0]);
	}

	public EmailAddress email() {
		return email;
	}

}
