package agitter.domain;

import java.util.HashSet;
import java.util.Set;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;


public class AgitterImpl implements Agitter {

	private final Set<User> _users = new HashSet<User>();
	private final EventsImpl _events = new EventsImpl();


	@Override
	public User signup(String name, String email, String password) throws Refusal {
		try {
			return login(email, password);
		} catch (Refusal e) {	}
		
		return createNewUser(name, email, password);
	}


	private User createNewUser(String name, String email, String password) throws Refusal {
		for (User existingUser : _users)
			if (existingUser.email().equals(email))
				throw new Refusal("Já existe um usuário cadastrado com este email: " + email);

		UserImpl result = new UserImpl(name, email, password);
		_users.add(result);
		return result;
	}


	@Override
	public User login(String email, String password) throws Refusal {
		for (User candidate : _users)
			if (candidate.email().equals(email) && candidate.isPassword(password))
				return candidate;

		throw new Refusal("Invalid Email or Password.");
	}

	
	@Override
	public Events events() {
		return _events;
	}

	
}
