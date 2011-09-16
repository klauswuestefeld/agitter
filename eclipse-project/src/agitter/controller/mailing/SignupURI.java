package agitter.controller.mailing;

import agitter.controller.RestRequest;
import agitter.domain.emails.EmailAddress;

public class SignupURI extends RestRequest {

	public SignupURI(EmailAddress email) {
		addParam("email", email.toString());
	}

	@Override
	protected String command() {
		return "signup";
	}
	
}
