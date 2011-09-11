package infra.simploy.tests;

import infra.simploy.DailyTrigger;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.testsupport.CleanTestBase;

public class DailyTriggerTest extends CleanTestBase {

	DailyTrigger subject = new DailyTrigger();
	
	@Test
	public void periodToWait() {
		assertMillisToWaitUntil(1000 * 60 * 60 * 24, "2011-01-01_06-00-00");
		assertMillisToWaitUntil(1000 * 60 * 60 * 6, "2011-01-01_00-00-00");
		assertMillisToWaitUntil(1000 * 60 * 60, "2011-01-01_05-00-00");
		assertMillisToWaitUntil(1000, "2011-01-01_05-59-59");
	}

	private void assertMillisToWaitUntil(int millisToWait, String time) {
		Clock.setForCurrentThread(time);
		assertEquals(millisToWait, subject.millisToWait());
	}
	
}