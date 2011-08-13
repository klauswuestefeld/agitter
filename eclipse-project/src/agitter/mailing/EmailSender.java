package agitter.mailing;

import agitter.domain.emails.EmailAddress;

public interface EmailSender {

	public void send(EmailAddress to, String subject, String body);

}
