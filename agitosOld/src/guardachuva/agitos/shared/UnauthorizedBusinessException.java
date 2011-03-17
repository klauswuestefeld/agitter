package guardachuva.agitos.shared;

public class UnauthorizedBusinessException extends BusinessException {
	
	private static final long serialVersionUID = 1L;

	public UnauthorizedBusinessException() {
	}
	
	public UnauthorizedBusinessException(String message) {
		super(message);
	}

}
