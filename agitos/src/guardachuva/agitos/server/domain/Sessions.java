package guardachuva.agitos.server.domain;

import guardachuva.agitos.server.crypt.Cryptor;
import guardachuva.agitos.server.crypt.CryptorException;
import guardachuva.agitos.shared.SessionToken;

import java.util.HashMap;

public class Sessions {

	HashMap<SessionToken, Session> _sessions = new HashMap<SessionToken, Session>();
	private int _nextSessionId = 0;

	public void logout(SessionToken sessionToken) {
		_sessions.remove(sessionToken);
	}

	public SessionToken create(User user) {
		/// FIXME: Validar se esta certo pegar as milis do relogio deste jeito
		Session session = new Session(user, _nextSessionId++, /* FIXME: my(Clock.class).time().currentValue()/**/0);
		SessionToken sessionToken = createToken(session);
		_sessions.put(sessionToken, session);
		return sessionToken;
	}

	private SessionToken createToken(Session session) {
		SessionToken sessionToken;
		try {
			sessionToken = new SessionToken(
				new Cryptor().encode(
					session.getLoggedUser().getEmail()
//FIXME: Descomentar assim que estiver ok o clock lendo do jornal
					 // + session.getId()
					 //+ session.getSessionCreated()
					)
				);
		} catch (CryptorException e) {
			throw new RuntimeException(e);
		}
		return sessionToken;
	}
	
	public boolean isValid(SessionToken sessionToken) {
		return _sessions.get(sessionToken) != null;
	}

	public User getLoggedUserOn(SessionToken sessionToken) {
		return _sessions.get(sessionToken).getLoggedUser();
	}

}
