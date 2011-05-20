/**
 * This code is property of Sumersoft Tecnologia Ltda.
 */
package agitter.mailing.tests;

import agitter.mailing.EmailSender;

public class EmailSenderMock implements EmailSender {

	private String to;
	private String subject;
	private String body;

	@Override
	public void send(String to, String subject, String body) {
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	public String to() {
		return to;
	}
	public String subject() {
		return subject;
	}
	public String body() {
		return body;
	}
}
