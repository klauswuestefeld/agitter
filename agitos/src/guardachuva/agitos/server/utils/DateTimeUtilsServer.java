package guardachuva.agitos.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtilsServer {

	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public static Date strToDate(String param) throws ParseException {
		return simpleDateFormat.parse(param);
	}

}
