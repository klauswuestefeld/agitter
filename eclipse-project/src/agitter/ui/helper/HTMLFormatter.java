package agitter.ui.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLFormatter {
	
	private final static String EMAIL_REGEX = 
			"\\b(([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+)";
	
	private final static String WEB_REGEX = 
			"\\b(" +
			 "(https?|ftp|gopher|telnet|file|notes|ms-help)://" +
			  "[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*" +
			  "[-A-Za-z0-9+&@#/%=~_()|])";
	
	// anything .internationaldomain or with two letters (country domains)  
	private final static String WWW_REGEX = 
			"\\b(" +
			  "(([-A-Za-z0-9]*)\\.)+" +
			  "(aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[-A-Za-z0-9]{2})"	+ 
			  "(\\/[-A-Za-z0-9+&@#/%?=~_()|!:,;]*" +
			  "[-A-Za-z0-9+&@#/%=~_()|])?)\\b(?!([.!?:,;]\\w))";
	
	private final static String NO_HTML = "\\<.*?\\>";
	
	private final static String HTML_A_TAG = "\\<a.*?\\>.*\\</a\\>";

	private static final String NEWLINE_REGEX = "\\r\\n|\\r|\\n";
	
	// replaceAll except for text in between <a> </a> tags
	public String replaceOutsideATAG(String text, String regex, String replacement) {
		StringBuffer temp = new StringBuffer();
		
		Pattern p = Pattern.compile(HTML_A_TAG);
		Matcher matcher = p.matcher(text);
		
		int lastIndex = 0;
		
		while (matcher.find()) {
			temp.append(text.substring(lastIndex, matcher.start()).replaceAll(regex, replacement));
			temp.append(matcher.group());
			
			lastIndex = matcher.end();
		}
		
		temp.append(text.substring(lastIndex, text.length()).replaceAll(regex, replacement));
		
		return temp.toString();
	}
	
	public String removeHTML(String text) {
		return text.replaceAll(NO_HTML,"");
	}
	
	public String addWEB(String text) {
		return replaceOutsideATAG(text, WEB_REGEX,"<a href='$1' target='_new'>$1</a>");
	}

	public String addMail(String text) {
		return replaceOutsideATAG(text, EMAIL_REGEX,"<a href='mailto:$1'>$1</a>");
	}
	
	public String addWWW(String text) {
		return replaceOutsideATAG(text, WWW_REGEX,"<a href='http://$1' target='_new'>$1</a>");
	}

	public String makeClickable(String text) {
		text = removeHTML(text);
		
		text = addMail(text);
		text = addWEB(text);
		text = addWWW(text);

	    return text;
	}
	
	public String makeClickableWithBr(String text) {
		return makeClickable(text).replaceAll(NEWLINE_REGEX, "<BR/>");
	}

}
