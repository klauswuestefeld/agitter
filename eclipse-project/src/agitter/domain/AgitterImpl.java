package agitter.domain;

import java.util.HashSet;
import java.util.Set;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.events.EventsImpl;


public class AgitterImpl implements Agitter {

	private final Set<String> _userEmails = new HashSet<String>();
	private final EventsImpl _events = new EventsImpl();


	@Override
	public AgitterSession signup(String name, String email, String password) {
		_userEmails.add(email);
		return createSession(name);
	}


	@Override
	public AgitterSession login(String email, String password) throws Refusal {
		if (!_userEmails.contains(email))
			throw new Refusal("Invalid Email or Password.");
		return createSession("Foo");
	}

	
	private AgitterSessionImpl createSession(String name) {
		return new AgitterSessionImpl(name, _events);
	}
	
}
