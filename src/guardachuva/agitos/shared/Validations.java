package guardachuva.agitos.shared;


public class Validations {
	
	public static String EMAIL_SEPARATOR = ", ";;
	
	// RFC 2822 compliant http://www.regular-expressions.info/email.html
	public final static String EMAIL_VALIDATION_REGEX = "([a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?)";
	
	public final static String EMAIL_AND_NAME_REGEX = "\"([a-zA-Z ]+)\" <" + EMAIL_VALIDATION_REGEX + ">";
	
	public final static String EMAIL_AND_OPTIONAL_NAME_REGEX =
		"^(?:" + EMAIL_AND_NAME_REGEX + "|" + EMAIL_VALIDATION_REGEX + ")$";
	
	public final static String DATE_VALIDATION_REGEX = "(0[1-9]|[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012]|[1-9])/(19|20)\\d{2}";
	
	public final static String TIME_VALIDATION_REGEX = "\\d{1,2}:\\d{1,2}";

	public static boolean validateEmail(String email) {
		return email.matches(EMAIL_VALIDATION_REGEX);
	}
	
	public static boolean validateEmailAndOptinalName(String text) {
		return text.matches(EMAIL_AND_OPTIONAL_NAME_REGEX);
	}
	
	public static boolean validateMultipleEmailAndOptinalName(String text) {
		for (String userString : text.split(EMAIL_SEPARATOR))
			if (!validateEmailAndOptinalName(userString))
				return false;
		
		return true;
	}

	public static boolean validateMinLength(String text, int minLength) {
		try {
			return text.trim().length() >= minLength;
		} catch (NullPointerException e) {
			return false;
		}
	}

	public static boolean validateDate(String date) {
		return date.matches(DATE_VALIDATION_REGEX);
	}
	
	public static boolean validateTime(String time) {
		return time.matches(TIME_VALIDATION_REGEX);
	}

}
