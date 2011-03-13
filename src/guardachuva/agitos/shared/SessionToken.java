package guardachuva.agitos.shared;

import java.io.Serializable;


public class SessionToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private String _token;
	
	public SessionToken() {
		
	}

	public SessionToken(String token) {
		_token = token;
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

	@Override
	public String toString() {
		return "SessionToken [_token=" + _token + "]";
	}

	public String getToken() {
		return _token;
	}
	
}
