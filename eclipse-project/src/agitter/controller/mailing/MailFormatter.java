package agitter.controller.mailing;

import agitter.controller.AuthenticationToken;
import agitter.domain.users.User;
import basis.lang.Clock;

public class MailFormatter {

	private static final long TWO_DAYS = 1000 * 60 * 60 * 24 * 2;

	protected String authUri(User u) {
		return new AuthenticationToken(u.email(), Clock.currentTimeMillis() + TWO_DAYS).asSecureURI();
	}

}