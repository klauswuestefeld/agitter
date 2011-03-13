package guardachuva.agitos.server.application;

import guardachuva.agitos.domain.User;

public class Session {

	private User _loggedUser;

	public Session(User user) {
		_loggedUser = user;
	}

	public User getLoggedUser() {
		return _loggedUser;
	}

}
