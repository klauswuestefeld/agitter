package agitter.domain;

import java.util.HashSet;
import java.util.Set;

import sneer.foundation.lang.exceptions.Refusal;


public class AgitterImpl implements Agitter {

	private Set<String> _userEmails = new HashSet<String>();


	@Override
	public AgitterSession signup(String name, String email, String password) {
		_userEmails.add(email);
		return new AgitterSessionImpl(name);
	}

	
	@Override
	public AgitterSession login(String email, String password) throws Refusal {
		if (_userEmails.contains(email))
			return new AgitterSessionImpl("Ana");
		throw new Refusal("Invalid Email or Password.");
	}

}
