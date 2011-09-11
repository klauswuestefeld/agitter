package infra.simploy;

import java.util.Calendar;
import java.util.GregorianCalendar;

import sneer.foundation.lang.Clock;

public class DailyTrigger extends Trigger {

	private static final int SIX_AM = 6;


	@Override
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
	
}
