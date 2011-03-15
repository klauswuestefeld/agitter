package guardachuva.agitos.server.domain;


public class Session {

	private User _loggedUser;

	public Session(User user) {
		_loggedUser = user;
	}

	public User getLoggedUser() {
		return _loggedUser;
	}

}
