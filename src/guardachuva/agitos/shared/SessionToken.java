package guardachuva.agitos.shared;

import java.io.Serializable;

public class SessionToken implements Serializable {

	private final String _token;

	public SessionToken(String token) {
		if (token == null) throw new IllegalArgumentException();
		_token = token;
	}

	@Override
	public int hashCode() {
		return _token.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		return _token != null && _token.equals(((SessionToken)obj)._token);
	}

	@Override
	public String toString() {
		return "SessionToken [_token=" + _token + "]";
	}

	public String getToken() {
		return _token;
	}

	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused") private SessionToken() { _token = null; }
}
