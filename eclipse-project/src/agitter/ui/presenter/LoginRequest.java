package agitter.ui.presenter;

import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;
import utils.EmailRequest;
import agitter.domain.emails.EmailAddress;

public class LoginRequest extends EmailRequest {


	public LoginRequest(EmailAddress email) {
		super(email);
	}
	
	public LoginRequest(Map<String, String[]> params) throws Refusal {
		super(params);
	}

	@Override
	protected String command() {
		return "login";
	}

}
