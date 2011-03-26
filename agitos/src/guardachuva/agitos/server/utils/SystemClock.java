package guardachuva.agitos.server.utils;

import java.util.Date;

public class SystemClock implements Clock {

	private static Clock clock = new SystemClock();
	
	private SystemClock() {
		
	}

	public static Clock getInstance() {
		return clock;
	}

	@Override
	public long millis() {
		return date().getTime();
	}

	@Override
	public Date date() {
		return new Date();
	}

}
