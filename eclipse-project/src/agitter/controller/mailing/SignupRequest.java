package agitter.controller.mailing;

import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.controller.AuthenticationToken;
import agitter.domain.emails.EmailAddress;

public class SignupRequest extends AuthenticationToken {

	public SignupRequest(Map<String, String[]> params) throws Refusal {
		super(params);
	}
	
	public SignupRequest(EmailAddress email) {
		super(email);
	}

	@Override
	protected String command() {
		return "signup";
	}

}
