package agitter.controller.mailing;

import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.controller.RestRequest;
import agitter.domain.emails.EmailAddress;

public class SignupRequest extends RestRequest {

	private final EmailAddress email;
	
	
	public SignupRequest(EmailAddress email) {
		this.email = email;
		addParamToUri("email", email.toString());
	}

	public static SignupRequest unmarshal(Map<String, String[]> params) throws Refusal {
		SignupRequest result = new SignupRequest(email(params));
		result.validate(params);
		return result;
	}

	private static EmailAddress email(Map<String, String[]> params) throws Refusal {
		return EmailAddress.email(params.get("email")[0]);
	}

	@Override
	protected String command() {
		return "signup";
	}

	public EmailAddress email() {
		return email;
	}

}
