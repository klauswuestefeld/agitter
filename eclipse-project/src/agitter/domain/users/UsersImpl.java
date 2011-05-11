package agitter.domain.users;

import java.util.HashSet;
import java.util.Set;

import sneer.foundation.lang.exceptions.Refusal;


public class UsersImpl implements Users {

	private final Set<User> _users = new HashSet<User>();


	@Override
	public User signup(String username, String email, String password) throws Refusal {
		try {
			return login(email, password);
		} catch (Refusal e) {	}
		
		return createNewUser(username, email, password);
	}


	private User createNewUser(String username, String email, String password) throws Refusal {
		for (User existingUser : _users)
			if (existingUser.email().equals(email))
				throw new Refusal("Já existe um usuário cadastrado com este email: " + email);

		UserImpl result = new UserImpl(username, email, password);
		_users.add(result);
		return result;
	}


	@Override
	public User login(String emailOrUsername, String password) throws UserNotFound, InvalidPassword {
		User user = emailOrUsername.contains("@") 
			? searchByEmail(emailOrUsername) 
			: searchByUsername(emailOrUsername);
		
		if (user == null)
			throw new UserNotFound("Usuário não encontrado: " + emailOrUsername);

		if (!user.isPassword(password))
			throw new InvalidPassword("Senha inválida.");

		return user;
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

}
