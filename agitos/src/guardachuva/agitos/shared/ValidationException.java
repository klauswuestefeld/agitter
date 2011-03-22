package guardachuva.agitos.shared;


public class ValidationException extends BusinessException {


	public ValidationException() {
	}
	
	private String[] _errors;

	public ValidationException(String clazz, String[] errors) {
		super("Erros encontrados. Valide antes da criação. \n" + clazz + ":" + errors);
		_errors = errors;
	}

	public ValidationException(String clazz, String error) {
		super("Erros encontrados. Valide antes da criação. \n" + clazz + ":" + error);
		_errors = new String[]{ error };
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String[] getValidationErrors() {
		return _errors;
	}

}
