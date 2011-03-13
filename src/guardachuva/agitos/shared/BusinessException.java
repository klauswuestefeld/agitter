package guardachuva.agitos.shared;

import java.io.Serializable;

public class BusinessException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public BusinessException() {
		
	}

	public BusinessException(String message) {
		super(message);
	}

}
