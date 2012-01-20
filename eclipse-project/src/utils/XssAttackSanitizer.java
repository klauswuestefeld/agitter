package utils;


public class XssAttackSanitizer {

	public static String ultraConservativeFilter(String string) {
		// Languages - Need to support other languages? Chinese, Japanese, Greek, Russian, Arabic, etc? Instead of this whitelist approach, consider blacklist approach only removing suspicious chars such as: <>&#(; and sequences such as "script", "javascript" and "eval" (ignore case). See http://ha.ckers.org/xss.html 
		return replaceAllWhitespacesWithSpace(string)
			.replaceAll("[^ , _ \\. \\- \\s @ a-z A-Z 0-9 áéíóúÁÉÍÓÚâêîôûÂÊÎÔÛàèìòùÀÈÌÒÙãõñÃÕÑçÇ]", "");
	}

	private static String replaceAllWhitespacesWithSpace(String string) {
		return string.replaceAll("\\s", " ");
	}

}
