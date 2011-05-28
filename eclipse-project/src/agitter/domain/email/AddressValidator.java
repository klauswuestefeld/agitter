package agitter.domain.email;

import sneer.foundation.lang.exceptions.Refusal;


public class AddressValidator {
	
	//Copied from com.vaadin.data.validator.EmailValidator. More considerations: http://www.regular-expressions.info/email.html
	private final static String EMAIL_VALIDATION_REGEX = "([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+";

	
	public static void validateEmail(String candidate) throws Refusal {
		if (!candidate.matches(EMAIL_VALIDATION_REGEX))
			throw new Refusal("Email inválido: " + candidate);
	}
	
}
