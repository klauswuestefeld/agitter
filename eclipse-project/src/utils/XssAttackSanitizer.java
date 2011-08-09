package utils;


public class XssAttackSanitizer {

	public static String ultraConservativeFilter(String string) {
		return replaceAllWhitespacesWithSpace(string)
			.replaceAll("[^ , _ \\. \\- \\s a-z A-Z 0-9 áéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõñÃÕÑàèìòùÀÈÌÒÙçÇ]", "");
	}

	private static String replaceAllWhitespacesWithSpace(String string) {
		return string.replaceAll("\\s", " ");
	}

}
