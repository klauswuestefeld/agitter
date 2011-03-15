package guardachuva.agitos.shared;



public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;
	
	protected BusinessException() {}

	public BusinessException(String message) {
		super(message);
	}

}
