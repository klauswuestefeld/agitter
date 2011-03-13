package guardachuva.agitos.shared;

import guardachuva.agitos.domain.User;

public class ValidationException extends BusinessException {


	private String[] _errors;

	public ValidationException(Class<User> clazz, String[] errors) {
		super("Erros encontrados. Valide antes da criação. \n" + clazz.getName() + ":" + errors);
		_errors = errors;
	}

	public ValidationException(Class<User> clazz, String error) {
		super("Erros encontrados. Valide antes da criação. \n" + clazz.getName() + ":" + error);
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
