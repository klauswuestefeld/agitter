package guardachuva.agitos.shared;

public class UserAlreadyExistsException extends BusinessException {

	public  UserAlreadyExistsException() {
	}
	
	public UserAlreadyExistsException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
