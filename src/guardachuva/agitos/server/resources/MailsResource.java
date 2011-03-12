package guardachuva.agitos.server.resources;

import guardachuva.mailer.core.Mail;

import java.util.HashMap;

public class MailsResource extends UnauthenticatedBaseResource {
	
	@Override
	protected Object doGet() throws Exception {
		HashMap<String, Mail> mails = _application.getScheduledMails().getScheduledMails();
		return mails;
	}

	@Override
	protected Object doPost() throws Exception {
		_application.getScheduledMails().deleteMail(_request.getParameter("key"));
		return true;
	}

}
