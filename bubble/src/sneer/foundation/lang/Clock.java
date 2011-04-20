package sneer.foundation.lang;

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

	public static void clearForCurrentThread() {
		_currentTime.remove();		
	}

}
