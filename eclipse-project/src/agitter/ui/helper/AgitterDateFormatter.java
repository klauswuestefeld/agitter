package agitter.ui.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AgitterDateFormatter {
	static private final DateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MMM/yyyy HH:mm", new Locale("pt","br"));

	public AgitterDateFormatter() {
		
	}
	
	public String format(Date d) {			
		String dateWithFeira = dateFormat.format(d);
		return dateWithFeira.replaceFirst("-feira", "");
	}
}
