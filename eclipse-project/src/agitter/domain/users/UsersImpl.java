package agitter.domain.users;

import java.util.HashSet;
import java.util.Set;

import sneer.foundation.lang.exceptions.Refusal;


public class UsersImpl implements Users {

	private final Set<User> _users = new HashSet<User>();


	@Override
	public User signup(String username, String email, String password) throws Refusal {
		try {
			return loginWithUsername(username, password);
		} catch (UserNotFound e) {
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

	
	private User createNewUser(String username, String email, String password) throws Refusal {
		for (User existingUser : _users)
			if (existingUser.email().equals(email))
				throw new Refusal("Já existe um usuário cadastrado com este email: " + email);
		
		UserImpl result = new UserImpl(username, email, password);
		_users.add(result);
		return result;
	}
	
	private void checkUser(User user, String emailOrUsername) throws UserNotFound {
		if (user == null)
			throw new UserNotFound("Usuário não encontrado: " + emailOrUsername);
	}
			
	private User searchByEmail(String email) {
		for (User candidate : _users)
			if (candidate.email().equals(email))
				return candidate;
		return null;
	}

	private User searchByUsername(String username) {
		for (User candidate : _users)
			if (candidate.username().equals(username))
				return candidate;
		return null;
	}
	
	private User login(User user, String emailOrUsername, String passwordAttempt) throws UserNotFound, InvalidPassword {
		checkUser(user, emailOrUsername);

		if (!user.isPassword(passwordAttempt))
			throw new InvalidPassword("Senha inválida.");

		return user;
	}
}
