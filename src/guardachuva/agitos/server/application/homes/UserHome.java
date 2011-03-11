package guardachuva.agitos.server.application.homes;

import guardachuva.agitos.domain.User;
import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.Validations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserHome {
	public HashMap<String, User> _users = new HashMap<String, User>();
	
	public boolean isKnownUser(String userName) {
		return _users.containsKey(userName);
	}
	
	public boolean isValidUser(String userName, String password) {
		try {
			return _users.get(userName).getPassword().equals(password);
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public List<User> produceMultipleUsers(String text) throws BusinessException {
		List<User> users = new ArrayList<User>();
		String[] userStrings = text.split(Validations.EMAIL_SEPARATOR);
		
		for (String userString : userStrings)
			users.add(produceUser(userString));
		
		return users;
	}
	
	public User produceUser(String text) throws BusinessException {
		Matcher matcher = Pattern.compile(Validations.EMAIL_AND_OPTIONAL_NAME_REGEX).matcher(text);
		
		if (matcher.find())
			return produceUserFromMatch(matcher);
		
		throw new BusinessException("O formato do e-mail é inválido.");
	}

	private User produceUserFromMatch(Matcher matcher) throws BusinessException {
		if (matcher.group(3) == null)
			return produceUser(matcher.group(1), matcher.group(2));
		else
			return produceUser(matcher.group(3), matcher.group(3));
	}
	
	private User produceUser(String name, String email) throws BusinessException {
		return produceUser(name, email, "123456", email);
	}

	public User produceUser(String name, String userName, String password, String email) throws BusinessException {
		User user = _users.get(email);
		if (user == null) {
			user = User.createFor(name, userName, password, email);
			_users.put(user.getEmail(), user);
		}
		return user;
	}

	public User authenticate(String userName, String password) throws UnauthorizedBusinessException {
		User user = _users.get(userName);
		if (user == null)
			throw new UnauthorizedBusinessException("Usuário não encontrado.");
		if (!user.getPassword().equals(password))
			throw new UnauthorizedBusinessException("Senha inválida.");
		return user;
	}

}