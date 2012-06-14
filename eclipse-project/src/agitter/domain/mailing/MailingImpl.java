package agitter.domain.mailing;

import java.util.Calendar;
import java.util.GregorianCalendar;

import basis.lang.Clock;



public class MailingImpl implements Mailing {

	private static final int PREFERRED_TIME = 16;
	private static final int TWELVE_HOURS = 1000 * 60 * 60 * 12;

	private long lastTimeScheduleWasSent = Long.MIN_VALUE;
	

	@Override
	public boolean shouldSendScheduleNow() {
		if (wasScheduleAlreadySentRecently())
			return false;
		
		return today().get(Calendar.HOUR_OF_DAY) >= PREFERRED_TIME;
	}


	@Override
	public void markScheduleSent() {
		lastTimeScheduleWasSent = now();
	}
	

	private GregorianCalendar today() {
		GregorianCalendar today = new GregorianCalendar();
		today.setTimeInMillis(now());
		return today;
	}
	
	
	private long now() {
		return Clock.currentTimeMillis();
	}


	private boolean wasScheduleAlreadySentRecently() {
		return lastTimeScheduleWasSent + TWELVE_HOURS > now();
	}

	
}
