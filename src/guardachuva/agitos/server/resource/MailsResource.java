package guardachuva.agitos.server.resource;

import guardachuva.mailer.core.Mail;
import guardachuva.mailer.core.ScheduledEmails;

import java.util.HashMap;

public class MailsResource extends UnauthenticatedBaseResource {
	
	@Override
	protected Object doGet() {
		HashMap<String, Mail> mails = ((ScheduledEmails)_application).getScheduledMails();
		return mails;
	}

	@Override
	protected Object doPost() {
		((ScheduledEmails)_application).deleteMail(_request.getParameter("key"));
		return true;
	}

}
