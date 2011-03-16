package guardachuva.agitos.server.domain;


public class Session {

	private User _loggedUser;
	private long _sessionCreated;
	private long _id;

	public Session(User user, long id, long sessionCreated) {
		_loggedUser = user;
		_id = id;
		_sessionCreated = sessionCreated;
	}

	public User getLoggedUser() {
		return _loggedUser;
	}

	public long getId() {
		return _id;
	}

	public long getSessionCreated() {
		return _sessionCreated;
	}

}
