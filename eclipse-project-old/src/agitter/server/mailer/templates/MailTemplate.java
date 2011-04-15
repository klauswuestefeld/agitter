package agitter.server.mailer.templates;

import agitter.shared.Mail;


public abstract class MailTemplate {

	public abstract String getSubject();

	protected abstract String getBodyTemplate();

	public String fillOutWith(Mail mail) {
		String result = getBodyTemplate();
		for (String key : mail.getKeys())
			result = result.replace("[" + key + "]",  mail.get(key));

		return result;
	}

}