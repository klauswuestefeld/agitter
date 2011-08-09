package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class XssAttackSanitizer {

	static {
		HTMLInputFilter.ALWAYS_MAKE_TAGS = false;
	}
	private static final HTMLInputFilter HTML_INPUT_FILTER = new HTMLInputFilter();

	public static String filterXSS(String html) {
//		return HTML_INPUT_FILTER.filter(html);

		
//		try {
//			return URLEncoder.encode(html, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			throw new IllegalStateException(e);
//		}
		
		return html;
	}

}
