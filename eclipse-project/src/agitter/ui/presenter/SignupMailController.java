package agitter.ui.presenter;

import static agitter.domain.emails.EmailAddress.email;

import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.Users;
import agitter.ui.mailing.MailSender;

public class SignupMailController {

	private static final String SUBJECT = "Ative sua Conta";
	private static final String LINK = "http://agitter.com/%REQUEST%";
	private static final String BODY = "Para ativar sua conta, clique no link:<br/><br/>"
		+ "<a href=\"" + LINK + "\">" + LINK + "</a><br/><br/><br/>"
		+ "<a href=\"http://agitter.com\">Agitter</a><br /><br />"
		+ "Bons agitos,<br />Equipe Agitter.";
	
	
	private final MailSender mailSender;
	private final Users users;

	
	public SignupMailController(MailSender mailSender, Users users) {
		this.mailSender = mailSender;
		this.users = users;
	}

	public void initiateSignup(EmailAddress mail, String password) {
		RestRequest uri = new SignupURI(mail);
		String body = BODY.replaceAll("%REQUEST%", uri.asRelativeURI());
		mailSender.send(mail, SUBJECT, body);
	}

	public void onRestInvocation(Map<String, String[]> params) throws Refusal {
		EmailAddress email = email(params.get("email")[0]);
		int recoverPassword;
		users.signup(email, "password");
	}
	
}
