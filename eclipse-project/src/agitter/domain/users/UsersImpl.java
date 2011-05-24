package agitter.domain.users;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.exceptions.Refusal;


public class UsersImpl implements Users {

	private final List<User> users = new ArrayList<User>();

	@Override
	public List<User> all() {
		return new ArrayList<User>(users);
	}

	@Override
	public User signup(String username, String email, String password) throws Refusal {
		try {
			return loginWithUsername(username, password);
		} catch(UserNotFound e) {
			return createNewUser(username, email, password);
		}
	}

	@Override
	public User loginWithUsername(String username, String password) throws UserNotFound, InvalidPassword {
		User user = searchByUsername(username);
		return login(user, username, password);
	}

	@Override
	public User loginWithEmail(String email, String password) throws UserNotFound, InvalidPassword {
		User user = searchByEmail(email);
		return login(user, email, password);
	}

	@Override
	public User findByUsername(String username) throws UserNotFound {
		User user = searchByUsername(username);
		checkUser(user, username);
		return user;
	}

	@Override
	public User findByEmail(String email) throws UserNotFound {
		User user = searchByEmail(email);
		checkUser(user, email);
		return user;
	}

	@Override
	public String userEncyptedInfo(User user) {
		return user.username();//TODO - Implement encryption
	}

	@Override
	public void unsubscribe(String userEncryptedInfo) throws UserNotFound {
		//TODO - Implement decrypt
		String username = userEncryptedInfo;
		User user = this.findByUsername(username);
		user.setInterestedInPublicEvents(false);
	}

	private User createNewUser(String username, String email, String password) throws Refusal {
		for(User existingUser : users) {
			if(existingUser.email().equals(email)) {
				throw new Refusal("Já existe um usuário cadastrado com este email: "+email);
			}
		}

		UserImpl result = new UserImpl(username, email, password);
		users.add(result);
		return result;
	}

	private void checkUser(User user, String emailOrUsername) throws UserNotFound {
		if(user==null) { throw new UserNotFound("Usuário não encontrado: "+emailOrUsername); }
	}

	private User searchByEmail(String email) {
		for(User candidate : users) { if(candidate.email().equals(email)) { return candidate; } }
		return null;
	}

	private User searchByUsername(String username) {
		for(User candidate : users) { if(candidate.username().equals(username)) { return candidate; } }
		return null;
	}

	private User login(User user, String emailOrUsername, String passwordAttempt) throws UserNotFound, InvalidPassword {
		checkUser(user, emailOrUsername);
		if(!user.isPassword(passwordAttempt)) { throw new InvalidPassword("Senha inválida."); }
		return user;
	}
}
