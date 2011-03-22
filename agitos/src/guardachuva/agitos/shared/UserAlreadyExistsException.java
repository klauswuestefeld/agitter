package guardachuva.agitos.shared;

public class UserAlreadyExistsException extends BusinessException {

	private static final String DEFAULT_MESSAGE = "Essa conta de usuário já existe.";

	public  UserAlreadyExistsException() {
		super(DEFAULT_MESSAGE);
	}
	
	public UserAlreadyExistsException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
