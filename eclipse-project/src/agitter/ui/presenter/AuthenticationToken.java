package agitter.ui.presenter;

import sneer.foundation.lang.exceptions.Refusal;
import utils.EmailRequest;
import utils.SecureRequest;
import agitter.domain.emails.EmailAddress;

public class AuthenticationToken extends EmailRequest {

	public static final String COMMAND = "auth";

	public AuthenticationToken(EmailAddress email) {
		super(email);
	}
	
	public AuthenticationToken(String uri) throws Refusal {
		super(SecureRequest.parseUri(COMMAND, uri));
	}

	@Override
	protected String command() {
		return COMMAND;
	}

}
