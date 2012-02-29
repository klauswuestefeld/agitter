package agitter.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.controller.mailing.PeriodicScheduleMailer;
import agitter.controller.mailing.tests.EmailSenderMock;
import agitter.domain.events.Event;
import agitter.domain.events.tests.EventsTestBase;
import agitter.domain.users.User;

public class PeriodicScheduleEmailTest extends EventsTestBase {

	private static final int ONE_HOUR = 1000 * 60 * 60;
	private static final int TWO_HOURS = ONE_HOUR * 2;
	private static final long ONE_DAY = ONE_HOUR * 24;
	private final long startTime = fourOClockOnThursday();


	@Before
	public void beforeMailingTest() {
		Clock.setForCurrentThread(startTime);
	}


	@Test
	public void sendingEmailsForTomorrow() throws Refusal, IOException {
		User leo = signup("leo");
		User klaus = signup("klaus");

		createEvent(klaus, "event1", startTime+10L);
		createEvent(klaus, "event2", startTime+11L);
		createEvent(leo, "churras", startTime+11L, klaus);
		createEvent(klaus, "event3", startTime+12L);
		createEvent(klaus, "event4", startTime+13L);
		createEvent(klaus, "eventNextDay", startTime+13L+TWO_HOURS+TWO_HOURS+ONE_DAY);

		Clock.setForCurrentThread(startTime+11 + TWO_HOURS);

		EmailSenderMock mock = sendEmailsAndCaptureLast();

		assertEquals("klaus@email.com", mock.to().toString());
		assertEquals("Agitos da Semana", mock.subject());
		assertContains(mock.body(), "event2", "churras", "event3", "event4");
		assertNotContains(mock.body(), "event1", "eventNextDay");
	}

	
	private void assertContains(String string, String... parts) {
		for (int i = 0; i < parts.length; i++)
			assertTrue("'" + parts[i] + "' not found in '" + string +"'", string.contains(parts[i]));
	}

	private void assertNotContains(String string, String... parts) {
		for (int i = 0; i < parts.length; i++)
			assertFalse("'" + parts[i] + "' found in '" + string +"'", string.contains(parts[i]));
	}


	@Test
	public void sendingEmailsToUnregisteredUsers() throws Refusal, IOException {
		User matias = signup("matias");

		createEvent(matias, "churras", startTime+11L, user("klaus@email.com"));
		Clock.setForCurrentThread(startTime+10);

		EmailSenderMock mock = sendEmailsAndCaptureLast();

		assertEquals("klaus@email.com", mock.to().toString());
		assertFragment("matias@email.com - churras", mock.body());
	}
	
	@Test
	public void xssAttackFiltering() throws Refusal, IOException {
		User leo = signup("leo");

		createEvent(leo, "<script>", startTime+11L, user("fulano@email.com"));
		Clock.setForCurrentThread(startTime+11);

		EmailSenderMock mock = sendEmailsAndCaptureLast();
		assertFragment("leo@email.com - ", mock.body());
	}


	private void assertFragment(String expectedFragment, String actual) {
		assertTrue(actual.contains(expectedFragment));
	}

	
	@Override
	protected Event createEvent(User owner, String description, long startTime, User... invitees) throws Refusal {
		return super.createEvent(agitter.events(), owner, description, startTime, invitees);
	}


	@Test
	public void onlyOnceADay() throws Refusal, IOException {
		User leo = signup("leo");
		createEvent(leo, "eventToday", startTime+11L);
		createEvent(leo, "eventTomorrow", startTime+11L+ONE_DAY);

		long tooEarly = startTime-10;
		Clock.setForCurrentThread(tooEarly);
		assertNull(sendEmailsAndCaptureLast().to());

		Clock.setForCurrentThread(startTime);
		assertNotNull(sendEmailsAndCaptureLast().to());
		assertNull(sendEmailsAndCaptureLast().to());

		Clock.setForCurrentThread(Clock.currentTimeMillis()+ONE_DAY);
		assertNotNull(sendEmailsAndCaptureLast().to());
	}

	@Test
	public void notInterestedInPublicEvents() throws Refusal, IOException {
		User leo = signup("leo");
		createEvent(leo, "eventToday", startTime+11L);
		createEvent(leo, "eventTomorrow", startTime+11L+ONE_DAY);

		Clock.setForCurrentThread(startTime);
		assertNotNull(sendEmailsAndCaptureLast().to());

		leo.setSubscribedToEmails(false);

		Clock.setForCurrentThread(Clock.currentTimeMillis()+ONE_DAY);
		assertNull(sendEmailsAndCaptureLast().to());
	}


	private long fourOClockOnThursday() {
		return new GregorianCalendar(2012, Calendar.FEBRUARY, 1, 16, 0).getTimeInMillis();
	}

	private EmailSenderMock sendEmailsAndCaptureLast() {
		EmailSenderMock emailSenderMock = new EmailSenderMock();
		PeriodicScheduleMailer daemon = new PeriodicScheduleMailer(agitter, emailSenderMock);
		daemon.sendEventsToHappenIn24Hours();
		return emailSenderMock;
	}

	private User signup(String username) throws Refusal {
		return agitter.users().signup(email(username+"@email.com"), "123");
	}
}

