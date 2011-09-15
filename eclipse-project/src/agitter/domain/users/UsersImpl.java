package agitter.domain.users;

import static infra.logging.LogInfra.getLogger;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public class UsersImpl implements Users {

	private static final String MSG_USUARIO_INATIVO = "Usuário ainda não foi ativado. Verifique em sua caixa de email o link de ativação.";
	private final List<User> users = new ArrayList<User>();

	
	@Override
	public List<User> all() {
		return new ArrayList<User>(users);
	}

	
	@Override
	public User signup(EmailAddress email, String password) throws Refusal {
		checkParameters(email, password);
		checkDuplicationAndSendActivationEmailIfInactive(email);

		UserImpl user = createUser(email, password);

		getLogger(this).info("Signup: "+email);

		return user;
	}


	private UserImpl createUser(EmailAddress email, String password) {
		UserImpl result = new UserImpl(email, password);
		users.add(result);
		return result;
	}

	
	@Override
	public User loginWithEmail(EmailAddress email, String password) throws UserNotFound, InvalidPassword, UserNotActive {
		User user = searchByEmail(email);
		return login(user, email.toString(), password);
	}


	@Override
	public User findByEmail(EmailAddress email) throws UserNotFound {
		User user = searchByEmail(email);
		checkUser(user, email.toString());
		return user;
	}


//	@Override
//	public String userEncyptedInfo(User user) {
//		return user.email().toString();//TODO - Implement encryption
//	}

	
	@Override
	public void unsubscribe(String userEncryptedInfo) throws UserNotFound {
		//TODO - Implement crypto
		User user = this.findByEmail(mail(userEncryptedInfo));
		user.setInterestedInPublicEvents(false);
	}


	@Override
	public User searchByEmail(EmailAddress email) {
		for(User candidate : users) { if(candidate.email().equals(email)) { return candidate; } }
		return null;
	}


	@Override
	public User produce(EmailAddress email) {
		User user = searchByEmail(email);
		return user == null
			? createInvitedUser(email)
			: user;
	}


	private void checkParameters(EmailAddress email, String password) throws Refusal {
		checkNotBlank("Email", email);
		checkNotBlank("Senha", password);
	}

	
	private void checkNotBlank(String field, Object value) throws Refusal {
		if(this.isBlank(value))
			throw new Refusal(field + " deve ser especificado");
	}

	
	private boolean isBlank(Object value) {
		return (value==null || value.toString().trim().isEmpty());
	}

	
	private void checkDuplicationAndSendActivationEmailIfInactive(EmailAddress email) throws Refusal {
		User user = searchByEmail(email);
		if (user==null) return;
		throw new Refusal("Já existe usuário cadastrado com este email: "+email);
	}

	
	private void checkUser(User user, String credential) throws UserNotFound {
		if (user==null) { throw new UserNotFound("Usuário não encontrado: " + credential); }
	}


	private User login(User user, String email, String passwordAttempt) throws UserNotFound, InvalidPassword, UserNotActive {
		checkUser(user, email);
		if(!user.isPasswordCorrect(passwordAttempt)) { throw new InvalidPassword("Senha inválida."); }
		if(!user.isActive()) { throw new UserNotActive(MSG_USUARIO_INATIVO); }
		getLogger(this).info("Login: "+email);
		return user;
	}

	
	private UserImpl createInvitedUser(EmailAddress email) {
		return createUser(email, null);
	}


	private EmailAddress mail(String email) throws UserNotFound {
		try {
			return EmailAddress.mail(email);
		} catch(Refusal refusal) {
			throw new UserNotFound("Usuário não encontrado: " + email);
		}
	}

}
