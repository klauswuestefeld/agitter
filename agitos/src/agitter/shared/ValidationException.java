package agitter.shared;

public class ValidationException extends BusinessException {

	private static final long serialVersionUID = 1L;
	private String[] _errors;

	public ValidationException(){
		
	}
	
	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String[] errors) {
		super(ConversionUtils.arrayToString(errors, "\n"));
	}
	
	public String[] getValidationErrors() {
		return _errors;
	}

}
