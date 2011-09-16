package agitter.ui.mailing.tests;

import agitter.domain.emails.EmailAddress;
import agitter.ui.mailing.MailSender;

public class MailSenderMock implements MailSender {

	private EmailAddress to;
	private String subject;
	private String body;

	@Override
	public void send(EmailAddress to, String subject, String body) {
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	public EmailAddress to() {
		return to;
	}
	public String subject() {
		return subject;
	}
	public String body() {
		return body;
	}
}
