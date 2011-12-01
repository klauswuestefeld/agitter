package agitter.controller.mailing;

import java.util.HashMap;
import java.util.Map;

import sneer.foundation.lang.exceptions.Refusal;
import utils.RestRequest;
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

	
	public void initiateSignup(EmailAddress email, String password) throws Refusal {
		checkDuplicatedSignup(email);
		passwordsByEmail.put(email, password);
		RestRequest req = new SignupRequest(email);
		String body = BODY.replaceAll("%REQUEST%", req.asSecureURI());
		emailSender.send(email, SUBJECT, body);
	}


	public User onRestInvocation(Map<String, String[]> params) throws Refusal {
		SignupRequest req = new SignupRequest(params);
		checkDuplicatedSignup(req.email());
		String password = passwordsByEmail.remove(req.email());
		if (password == null) throw new Refusal("Código de ativação expirado. Faça seu cadastro novamente.");
		return users.signup(req.email(), password);
	}

	
	private void checkDuplicatedSignup(EmailAddress email) throws Refusal {
		User user = users.searchByEmail(email);
		if (user != null && user.hasSignedUp()) throw new Refusal("Já existe um usuário cadastrado com este email: " + email);
	}
	
}
