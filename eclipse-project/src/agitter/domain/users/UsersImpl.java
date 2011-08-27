package agitter.domain.users;

import static infra.logging.LogInfra.getLogger;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public class UsersImpl implements Users {

	private final List<User> users = new ArrayList<User>();

	
	@Override
	public List<User> all() {
		return new ArrayList<User>(users);
	}

	
	@Override
	public User signup(String username, EmailAddress email, String password) throws Refusal {
		checkParameters(username, email, password);
		checkDuplication(username, email);

		UserImpl result = createUser(username, email, password);

		getLogger(this).info("Signup: "+username+" - email: "+email);
		return result;
	}

	
	private UserImpl createUser(String username, EmailAddress email, String password) {
		UserImpl result = new UserImpl(username, email, password);
		users.add(result);
		return result;
	}

	
	@Override
	public User loginWithUsername(String username, String password) throws UserNotFound, InvalidPassword {
		User user = searchByUsername(username);
		return login(user, username, password);
	}


	@Override
	public User loginWithEmail(EmailAddress email, String password) throws UserNotFound, InvalidPassword {
		User user = searchByEmail(email);
		return login(user, email.toString(), password);
	}


	@Override
	public User findByUsername(String username) throws UserNotFound {
		User user = searchByUsername(username);
		checkUser(user, username);
		return user;
	}


	@Override
	public User findByEmail(EmailAddress email) throws UserNotFound {
		User user = searchByEmail(email);
		checkUser(user, email.toString());
		return user;
	}


	@Override
	public String userEncyptedInfo(User user) {
		return user.username();//TODO - Implement encryption
	}

	
	@Override
	public void unsubscribe(String userEncryptedInfo) throws UserNotFound {
		//TODO - Implement crypto
		String username = userEncryptedInfo;
		User user = this.findByUsername(username);
		user.setInterestedInPublicEvents(false);
	}

	
	private void checkParameters(final String username, final EmailAddress email, final String password) throws Refusal {
		checkNotBlank("Username", username);
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

	
	private void checkDuplication(String username, EmailAddress email) throws Refusal {
		if(searchByUsername(username)!=null) {
			throw new Refusal("Já existe usuário cadastrado com este username: "+username);
		}
		if(searchByEmail(email)!=null) { throw new Refusal("Já existe usuário cadastrado com este email: "+email); }
	}

	
	private void checkUser(User user, String credential) throws UserNotFound {
		if (user==null) { throw new UserNotFound("Usuário não encontrado: " + credential); }
	}

	
	@Override
	public User searchByEmail(EmailAddress email) {
		for(User candidate : users) { if(candidate.email().equals(email)) { return candidate; } }
		return null;
	}

	
	private User searchByUsername(String username) {
		for(User candidate : users)
			if(username.equals(candidate.username()))
				return candidate;
		return null;
	}

	
	private User login(User user, String emailOrUsername, String passwordAttempt) throws UserNotFound, InvalidPassword {
		checkUser(user, emailOrUsername);
		if(!user.isPassword(passwordAttempt)) { throw new InvalidPassword("Senha inválida."); }
		getLogger(this).info("Login: "+emailOrUsername);
		return user;
	}

	
	@Override
	public User produce(EmailAddress email) {
		User user = searchByEmail(email);
		return user == null
			? createInvitedUser(email)
			: user;
	}

	
	private UserImpl createInvitedUser(EmailAddress email) {
		return createUser(null, email, null);
	}


	public void migrate() {
		for (User user : users)
			((UserImpl)user).migrate();
	}

}
