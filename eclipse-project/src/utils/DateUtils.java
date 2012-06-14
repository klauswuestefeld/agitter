package utils;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;

import java.util.Calendar;
import java.util.GregorianCalendar;

import basis.lang.Clock;



public class DateUtils {

	public static boolean isToday(int dayOfWeek) {
		return today().get(DAY_OF_WEEK) == dayOfWeek;
	}

	public static long daysFromNow(int days) {
		GregorianCalendar cal = today();
		cal.add(DATE, days);
		return cal.getTimeInMillis();
	}

	private static GregorianCalendar today() {
		GregorianCalendar cal = asCalendar(Clock.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static GregorianCalendar asCalendar(long millis) {
		GregorianCalendar ret = new GregorianCalendar();
		ret.setTimeInMillis(millis);
		return ret;
	}

}
