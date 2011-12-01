package agitter.controller.mailing;

import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;
import utils.EmailRequest;
import agitter.domain.emails.EmailAddress;

public class SignupRequest extends EmailRequest {

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
