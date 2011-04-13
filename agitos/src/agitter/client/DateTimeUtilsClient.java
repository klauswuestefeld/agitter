package agitter.client;

import java.util.Date;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DateTimeUtilsClient {

	public static String dateToStr(Date date) {
		return DateTimeFormat.getFormat("dd/MM/yyyy HH:mm").format(date);
	}
	
	public static Date strToDate(String dateTime ) {
		try {
			return DateTimeFormat.getFormat("dd/MM/yyyy HH:mm").parse(dateTime);
		} catch(Exception e) {
			return null;
		}
	}
	
	public static Date strToDate(String date, String time) {
		return strToDate(date + " " + time);
	}

	public static DateTimeFormat getDateFormat() {
		return DateTimeFormat.getFormat("dd/MM/yyyy");
	}

}
