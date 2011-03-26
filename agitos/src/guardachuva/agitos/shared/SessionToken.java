package guardachuva.agitos.shared;

import java.io.Serializable;


import com.google.gwt.user.client.rpc.IsSerializable;

public class SessionToken extends Immutable implements Serializable, IsSerializable {

	private String _token;
	public static final String COOKIE_NAME = "sessionToken";

	public SessionToken(String token) {
		if (token == null) throw new IllegalArgumentException();
		_token = token;
	}

	@Override
	public String toString() {
		return "SessionToken [_token=" + _token + "]";
	}

	public String getToken() {
		return _token;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_token == null) ? 0 : _token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionToken other = (SessionToken) obj;
		if (_token == null) {
			if (other._token != null)
				return false;
		} else if (!_token.equals(other._token))
			return false;
		return true;
	}


	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused") private SessionToken() { _token = null; }
}
