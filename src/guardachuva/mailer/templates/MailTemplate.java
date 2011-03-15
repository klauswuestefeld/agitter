package guardachuva.mailer.templates;

import guardachuva.agitos.shared.Mail;


public abstract class MailTemplate {

	public abstract String getSubject();

	protected abstract String getBodyTemplate();

	public String fillOutWith(Mail mail) {
		String result = getBodyTemplate();
		for (String key : mail.getKeys())
			result = result.replace("[" + (String) key + "]", (String) mail.get(key));

		return result;
	}

}