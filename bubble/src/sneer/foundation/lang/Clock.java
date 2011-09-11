package sneer.foundation.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Clock {
	
	private static ThreadLocal<Long> _currentTime = new ThreadLocal<Long>();

	public static long currentTimeMillis() {
		Long result = _currentTime.get();
		return result == null
			? System.currentTimeMillis()
			: result;
	}
	
	public static void setForCurrentThread(long millis) {
		_currentTime.set(millis);
	}

	public static void setForCurrentThread(String formattedDate) {
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").parse(formattedDate);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		Clock.setForCurrentThread(date.getTime());
	}

	public static Long memento() {
		return _currentTime.get();		
	}

	public static void restore(Long memento) {
		if (memento == null) {
			_currentTime.remove();
			return;
		}
		_currentTime.set(memento);
	}

	public static void clearForCurrentThread() {
		_currentTime.remove();
	}

}
