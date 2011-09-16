package agitter.controller.mailing.tests;

import agitter.controller.mailing.EmailSender;
import agitter.domain.emails.EmailAddress;

public class EmailSenderMock implements EmailSender {

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
