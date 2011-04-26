package agitter.domain;



public class AgitterSessionImpl implements AgitterSession {

	private final EventsImpl _events = new EventsImpl();
	private final String _userName;

	
	AgitterSessionImpl(String userName) {
		_userName = userName;
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
