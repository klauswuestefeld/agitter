package agitter.domain.users;

import static infra.logging.LogInfra.getLogger;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.exceptions.Refusal;
import utils.Encoder;
import agitter.domain.emails.EmailAddress;

public class UsersImpl implements Users {

	private final List<User> users = new ArrayList<User>();
	
	@Override
	public List<User> all() {
		return new ArrayList<User>(users);
	}

	
	@Override
	public User signup(EmailAddress email, String rawPassword) throws Refusal {
		checkParameters(email, rawPassword);
		checkDuplicateSignup(email);

		UserImpl user = (UserImpl)produce(email);
		user.setPassword(encrypt(rawPassword));
		
		getLogger(this).info("Signup: "+email);

		return user;
	}


	private String encrypt(String rawPassword) {
		return new Encoder().encryptPassword(rawPassword); 
	}


	private UserImpl createUser(EmailAddress email, String rawPassword) {
		UserImpl result = new UserImpl(email, encrypt(rawPassword));
		users.add(result);
		return result;
	}

	
	@Override
	public User loginWithEmail(EmailAddress email, String rawPassword) throws UserNotFound, InvalidPassword, UserNotActive {
		User user = searchByEmail(email);
		return login(user, email.toString(), encrypt(rawPassword));
	}
	
	@Override
	public User loginWithEmail(EmailAddress email) throws UserNotActive, UserNotFound {
		User user = searchByEmail(email);
		return login(user, email.toString());
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

	
	private void checkDuplicateSignup(EmailAddress email) throws Refusal {
		User user = searchByEmail(email);
		if (user==null) return;
		if (!user.hasSignedUp()) return;
		throw new Refusal("Já existe usuário cadastrado com este email: "+email);
	}

	
	private void checkUser(User user, String credential) throws UserNotFound {
		if (user == null)
			throw new UserNotFound("Usuário não encontrado: " + credential);
	}


	private User login(User user, String email, String encryptedPasswordAttempt) throws UserNotFound, InvalidPassword, UserNotActive {
		checkUserIsValidAndActive(user, email);
		if(!user.isPasswordCorrect(encryptedPasswordAttempt)) { throw new InvalidPassword("Senha inválida."); }
		getLogger(this).info("Login: "+email);
		return user;
	}

	private User login(User user, String email) throws UserNotFound, UserNotActive {
		checkUserIsValidAndActive(user, email);
		getLogger(this).info("Auto login: "+email);
		return user;
	}

	private void checkUserIsValidAndActive(User user, String email) throws UserNotFound, UserNotActive {
		checkUser(user, email);
		if(!user.hasSignedUp()) { throw new UserNotActive("Usuário não encontrado: " + email); }
	}

	
	private UserImpl createInvitedUser(EmailAddress email) {
		return createUser(email, null);
	}


	private EmailAddress mail(String email) throws UserNotFound {
		try {
			return EmailAddress.email(email);
		} catch(Refusal refusal) {
			throw new UserNotFound("Usuário não encontrado: " + email);
		}
	}


	public void migrateSchemaIfNecessary() {
		for (User u : this.all()) 
			((UserImpl) u).migrateSchemaIfNecessary();
	}

}
