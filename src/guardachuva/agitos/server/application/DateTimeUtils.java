package guardachuva.agitos.server.application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

	private static SimpleDateFormat _dateFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public static Date strToDate(String string) throws ParseException {
		return _dateFormater.parse(string);
	}

}
