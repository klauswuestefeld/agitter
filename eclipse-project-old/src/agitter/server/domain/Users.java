package agitter.server.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import agitter.shared.UnauthorizedBusinessException;
import agitter.shared.ValidationException;
import agitter.shared.Validations;

public class Users  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public HashMap<String, User> _users = new HashMap<String, User>();
	
	public boolean isKnownUser(String userName) {
		return _users.containsKey(userName);
	}
	
	public boolean isValidUser(String userName, String password) {
		try {
			return _users.get(userName).isValidPassword(password);
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public List<User> produceMultipleUsers(String text) throws ValidationException {
		List<User> users = new ArrayList<User>();
		String[] userStrings = text.split(Validations.EMAIL_SEPARATOR);
		
		for (String userString : userStrings) {
			if(!userString.trim().isEmpty())
				users.add(produceUser(userString));
		}
		
		return users;
	}
	
	public User produceUser(String text) throws ValidationException {
		String emailText = text.trim();
		Matcher matcher = Pattern.compile(Validations.EMAIL_AND_OPTIONAL_NAME_REGEX).matcher(emailText);
		
		if (matcher.find())
			return produceUserFromMatch(matcher);
		
		throw new ValidationException("O formato do e-mail é inválido.");
	}

	private User produceUserFromMatch(Matcher matcher) throws ValidationException {
		return matcher.group(3) == null
			? produceUser(matcher.group(1), matcher.group(2))
			: produceUser(matcher.group(3), matcher.group(3));
	}
	
	public User produceUser(String name, String email) throws ValidationException {
		return produceUser(name, email, User.PASSWORD_DEFAULT, email);
	}

	public User produceUser(String name, String userName, String password, String email) throws ValidationException {
		User user = _users.get(email);
		if (user == null) {
			user = User.createFor(name, userName, password, email);
			_users.put(user.getEmail(), user);
		}
		return user;
	}

	public User authenticate(String email, String password) throws UnauthorizedBusinessException {
		User user = _users.get(email);
		if (user == null || !user.isValidPassword(password)) 
			throw new UnauthorizedBusinessException("Usuário ou senha inválidos.");
		return user;
	}

}