package agitter.controller.mailing;

import java.io.IOException;

import agitter.domain.emails.EmailAddress;

public class ForgotPasswordMailSender {

	private static final String SUBJECT = "Lembrete de Senha";
	private static final String BODY =
		"Você solicitou que nós enviássemos sua senha.<br />" +
		"Sua senha é: %PASSWORD% <br/><br/>" +
		"<a href=\"http://agitter.com\">Agitter</a><br /><br />" +
		"Bons agitos,<br />Equipe Agitter.";

	
	public static void send(EmailSender sender, EmailAddress to, String password) throws IOException {
		String body = BODY.replaceAll("%PASSWORD%", password);
		sender.send(to, SUBJECT, body);
	}

}
