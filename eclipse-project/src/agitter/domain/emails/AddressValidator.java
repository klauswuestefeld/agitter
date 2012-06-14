package agitter.domain.emails;

import java.util.regex.Pattern;

import basis.lang.exceptions.Refusal;



public class AddressValidator {
	
	//Copied from com.vaadin.data.validator.EmailValidator. More considerations: http://www.regular-expressions.info/email.html
	public final static Pattern VALID_EMAIL = Pattern.compile("([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+");

	
	public static void validateEmail(String candidate) throws Refusal {
		if (candidate == null || candidate.trim().length() == 0)
			throw new Refusal("Email deve ser preenchido.");
		if (!isValidEmail(candidate))
			throw new Refusal("Email inválido: " + candidate);
	}


	public static boolean isValidEmail(String candidate) {
		return VALID_EMAIL.matcher(candidate).matches();
	}
	
}
