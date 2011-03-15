package guardachuva.agitos.server.resource;

import guardachuva.agitos.shared.Mail;
import guardachuva.agitos.shared.ScheduledEmails;

import java.util.HashMap;

public class MailsResource extends UnauthenticatedBaseResource {
	
	@Override
	protected Object doGet() throws Exception {
		HashMap<String, Mail> mails = ((ScheduledEmails)_application).getScheduledMails();
		return mails;
	}

	@Override
	protected Object doPost() throws Exception {
		((ScheduledEmails)_application).deleteMail(_request.getParameter("key"));
		return true;
	}

}
