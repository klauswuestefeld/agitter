package agitter.ui.presenter;

import agitter.domain.emails.EmailAddress;

public class SignupURI extends RestRequest {

	public SignupURI(EmailAddress mail) {
		addParam("mail", mail.toString());
	}

	@Override
	protected String command() {
		return "signup";
	}
	
}
