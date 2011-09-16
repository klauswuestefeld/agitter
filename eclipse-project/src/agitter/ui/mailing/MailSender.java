package agitter.ui.mailing;

import agitter.domain.emails.EmailAddress;

public interface MailSender {

	public void send(EmailAddress to, String subject, String body);

}
