package agitter.domain.email;


public class EmailValidator {
	
	//Copied from com.vaadin.data.validator.EmailValidator. More considerations: http://www.regular-expressions.info/email.html
	private final static String EMAIL_VALIDATION_REGEX = "([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+";

	
	public static boolean validateEmail(String candidate) {
		return candidate.matches(EMAIL_VALIDATION_REGEX);
	}
	
}
