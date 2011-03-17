package guardachuva.agitos.shared;

import com.google.gwt.user.client.rpc.IsSerializable;



public class BusinessException extends Exception implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	protected BusinessException() {}

	public BusinessException(String message) {
		super(message);
	}

}
