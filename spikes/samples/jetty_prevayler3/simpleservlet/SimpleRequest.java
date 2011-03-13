package samples.jetty_prevayler3.simpleservlet;

import java.io.Serializable;

public class SimpleRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String _requestURI;

	public SimpleRequest(String requestURI) {
		_requestURI = requestURI;
	}

	public String requestURI() {
		return _requestURI;
	}

}
