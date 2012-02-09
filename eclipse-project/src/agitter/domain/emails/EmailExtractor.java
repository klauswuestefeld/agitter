package agitter.domain.emails;

import static agitter.domain.emails.AddressValidator.VALID_EMAIL;
import static java.util.Locale.ENGLISH;

import java.util.regex.Matcher;


public class EmailExtractor {

	
	public interface Visitor {
		void visit(String name, EmailAddress emailAddress);
	}


	public static void extractAddresses(String string, Visitor visitor) {
		Matcher matcher = VALID_EMAIL.matcher(string);

		int nextNameRegion = 0;
		while (matcher.find()) {
			String name = extractName(string.substring(nextNameRegion, matcher.start()));
			visitor.visit(name, EmailAddress.certain(matcher.group()));
			nextNameRegion = matcher.end();
		}
	}

	
	private static String extractName(String string) {
		StringBuffer name = new StringBuffer();
		for (char c : string.toCharArray()) {
			if (isIgnored(c)) continue;
			if (isSeparator(c)) { clear(name); continue; }
			if (c == '@') return null; //Invalid email in the middle of a name region.
			name.append(c);
		}
		String result = name.toString().trim();
		result = removeFieldLabels(result);
		return (result.length() < 3) ? null : result;
	}


	private static StringBuffer clear(StringBuffer name) {
		return name.delete(0, name.length());
	}

	
	private static String removeFieldLabels(String result) {
		String lower = result.toLowerCase(ENGLISH);
		if (lower.startsWith("para ")) return result.substring(5);
		if (lower.startsWith("to ")) return result.substring(3);
		if (lower.startsWith("de ")) return result.substring(3);
		if (lower.startsWith("from ")) return result.substring(5);
		if (lower.startsWith("cc ")) return result.substring(3);
		if (lower.startsWith("bcc ")) return result.substring(4);
		return result;
	}


	private static boolean isSeparator(char c) {
		return ":[]{}(),;#/\t\\".indexOf(c) != -1;
	}

	
	private static boolean isIgnored(char c) {
		return "<>\"\n".indexOf(c) != -1;
	}

}
