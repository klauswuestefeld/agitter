package guardachuva.agitos.server.application;

import java.util.HashMap;

import guardachuva.agitos.domain.User;
import guardachuva.agitos.shared.SessionToken;

public class Sessions {

	HashMap<SessionToken, Session> _sessions = new HashMap<SessionToken, Session>();

	public void logout(SessionToken sessionToken) {
		_sessions.remove(sessionToken);
	}

	public SessionToken create(User user) {
		SessionToken sessionToken = new SessionToken(Integer.toHexString(user.hashCode()));
		Session session = new Session(user);
		_sessions.put(sessionToken, session);
		return sessionToken;
	}
	
	public boolean isValid(SessionToken sessionToken) {
		return _sessions.get(sessionToken) != null;
	}

	public User getLoggedUserOn(SessionToken sessionToken) {
		return _sessions.get(sessionToken).getLoggedUser();
	}

}
