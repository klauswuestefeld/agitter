package agitter.domain;

import agitter.domain.events.Events;



public class AgitterSessionImpl implements AgitterSession {

	private final Events _events;
	private final String _userName;

	
	AgitterSessionImpl(String userName, Events events) {
		_userName = userName;
		_events = events;
	}

	
	@Override
	public Events events() {
		return _events;
	}

	@Override
	public void logout() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public String userName() {
		return _userName;
	}

}
