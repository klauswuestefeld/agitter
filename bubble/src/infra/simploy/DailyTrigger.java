package infra.simploy;

import java.util.Calendar;
import java.util.GregorianCalendar;

import sneer.foundation.lang.Clock;

public class DailyTrigger extends Trigger {

	private static final int SIX_AM = 6;


	@Override
	synchronized
	void waitFor() {
		waitQuietly(millisToWait());
	}
	
	
	public long millisToWait() {
		long now = Clock.currentTimeMillis();
		GregorianCalendar when = new GregorianCalendar();
		when.setTimeInMillis(now);
		if (when.get(Calendar.HOUR_OF_DAY) >= SIX_AM)
			when.add(Calendar.DATE, 1);
		when.set(Calendar.HOUR_OF_DAY, SIX_AM);
		when.set(Calendar.MINUTE, 0);
		when.set(Calendar.SECOND, 0);
		return when.getTime().getTime() - Clock.currentTimeMillis();
	}


	@Override
	String status() {
		return "(No pulls are being made by this Simploy. Trusting pulls are being made by someone else.)\n"
			+ timeUntilNextBuild();
	}


	private String timeUntilNextBuild() {
		long millis = millisToWait();
		long seconds = millis / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		return hours + "h " + (minutes % 60) + "min " + (seconds % 60) + "sec to next build.\n";
	}
	
}
