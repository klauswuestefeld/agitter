package agitter.controller.mailing;

import static agitter.domain.emails.EmailAddress.email;

import java.util.HashMap;
import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.controller.RestRequest;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.Users;

public class SignupEmailController {

	private static final String SUBJECT = "Ative sua Conta";
	private static final String LINK = "http://agitter.com/%REQUEST%";
	private static final String BODY = "Para ativar sua conta, clique no link:<br/><br/>"
		+ "<a href=\"" + LINK + "\">" + LINK + "</a><br/><br/><br/>"
		+ "<a href=\"http://agitter.com\">Agitter</a><br /><br />"
		+ "Bons agitos,<br />Equipe Agitter.";
	
	
	private final Map<EmailAddress, String> passwordsByEmail = new HashMap<EmailAddress, String>();
	private final EmailSender emailSender;
	private final Users users;

	
	public SignupEmailController(EmailSender emailSender, Users users) {
		this.emailSender = emailSender;
		this.users = users;
	}

	
	public void initiateSignup(EmailAddress mail, String password) {
		passwordsByEmail.put(mail, password);
		RestRequest uri = new SignupURI(mail);
		String body = BODY.replaceAll("%REQUEST%", uri.asRelativeURI());
		emailSender.send(mail, SUBJECT, body);
	}

	
	public User onRestInvocation(Map<String, String[]> params) throws Refusal {
		EmailAddress email = email(params.get("email")[0]);
		String password = passwordsByEmail.get(email);
		if (password == null) throw new Refusal("Código de ativação expirado. Tente novamente.");
		return users.signup(email, password);
	}
	
}
