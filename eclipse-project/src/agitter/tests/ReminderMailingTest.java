package agitter.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import basis.lang.Clock;
import basis.lang.exceptions.Refusal;

import agitter.controller.mailing.ReminderMailer;
import agitter.controller.mailing.tests.EmailSenderMock;
import agitter.domain.events.Event;
import agitter.domain.events.tests.EventsTestBase;
import agitter.domain.users.User;

public class ReminderMailingTest extends EventsTestBase {

	private static final int ONE_HOUR = 1000 * 60 * 60;
	private static final long ONE_DAY = ONE_HOUR * 24;
	private final long startTime = fourOClockOnWednesday();


	@Before
	public void beforeMailingTest() {
		Clock.setForCurrentThread(startTime);
	}


	@Test
	public void sendingEmailReminders() throws Refusal, IOException {
		User fred = signup("fred");

		createEvent(fred, "wednesday event", date(8, 16, 0));
		createEvent(fred, "today event", date(9, 23, 59));
		createEvent(fred, "friday event 1", date(10, 0, 0));
		createEvent(fred, "friday event 2", date(10, 23, 59));
		createEvent(fred, "saturday event", date(11, 0, 0));
		createEvent(fred, "sunday event", date(12, 0, 0));

		long thursday = date(9, 16, 0);
		Clock.setForCurrentThread(thursday);
		EmailSenderMock mock = sendEmailsAndCaptureLast();
		
		assertEquals("fred@email.com", mock.to().toString());
		assertEquals("Lembretes de Agito", mock.subject());
		assertContains(mock.body(), "friday event 1", "friday event 2");
		assertNotContains(mock.body(), "wednesday event", "today event", "saturday event");
	}

	
	@Test
	public void sendingEmailRemindersOnWeekends() throws Refusal, IOException {
		User fred = signup("fred");

		createEvent(fred, "friday event", date(10, 23, 59));
		createEvent(fred, "saturday event", date(11, 0, 0));
		createEvent(fred, "sunday event", date(12, 0, 0));
		createEvent(fred, "monday event", date(13, 0, 0));

		long friday = date(10, 16, 0);
		Clock.setForCurrentThread(friday);
		assertRemindersForSaturdayAndSundayAreSent();

		long saturday = date(11, 16, 0);
		Clock.setForCurrentThread(saturday);
		assertNoRemindersAreSent();
	}


	private void assertRemindersForSaturdayAndSundayAreSent() {
		EmailSenderMock mock = sendEmailsAndCaptureLast();
		assertContains(mock.body(), "saturday event", "sunday event");
		assertNotContains(mock.body(), "friday event", "monday event");
	}

	
	private void assertNoRemindersAreSent() {
		EmailSenderMock mock = sendEmailsAndCaptureLast();
		assertNull(mock.body());
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

		createEvent(matias, "churras", startTime+ONE_DAY, user("klaus@email.com"));
		Clock.setForCurrentThread(startTime+10);

		EmailSenderMock mock = sendEmailsAndCaptureLast();

		assertEquals("klaus@email.com", mock.to().toString());
		assertFragment("matias@email.com - churras", mock.body());
	}

	@Test
	public void usersNotInterestedOrNotGoingShouldNotGetReminders() throws Refusal, IOException {
		User matias = signup("matias");

		User user1 = user("klaus@email.com");
		User user2 = user("leo@email.com");
		Event event = createEvent(matias, "churras", startTime+ONE_DAY, user1, user2);
		event.setNotInterested(user1);
		event.setAttendance(user2, startTime+ONE_DAY, Event.Attendance.NOT_GOING);
				
		Clock.setForCurrentThread(startTime+10);
		EmailSenderMock mock = sendEmailsAndCaptureLast();

		assertFragmentNotContained("klaus@email.com", mock.to().toString());
		assertFragmentNotContained("leo@email.com", mock.to().toString());
	}
	
	
	@Test
	public void xssAttackFiltering() throws Refusal, IOException {
		User leo = signup("leo");

		createEvent(leo, "<script>", startTime+ONE_DAY, user("fulano@email.com"));
		Clock.setForCurrentThread(startTime+11);

		EmailSenderMock mock = sendEmailsAndCaptureLast();
		assertFragment("leo@email.com - ", mock.body());
	}


	private void assertFragment(String expectedFragment, String actual) {
		assertTrue(actual.contains(expectedFragment));
	}

	
	private void assertFragmentNotContained(String prohibitedFragment, String actual) {
		assertFalse(actual.contains(prohibitedFragment));
	}

	
	@Override
	protected Event createEvent(User owner, String description, long startTime, User... invitees) throws Refusal {
		return super.createEvent(agitter.events(), owner, description, startTime, invitees);
	}


	@Test
	public void onlyOnceADay() throws Refusal, IOException {
		User leo = signup("leo");
		createEvent(leo, "eventTomorrow", startTime+ONE_DAY);
		createEvent(leo, "eventAfterTomorrow", startTime+ONE_DAY+ONE_DAY);

		long tooEarly = startTime - 10;
		Clock.setForCurrentThread(tooEarly);
		assertNull(sendEmailsAndCaptureLast().to());

		Clock.setForCurrentThread(startTime);
		assertNotNull(sendEmailsAndCaptureLast().to());
		assertNull(sendEmailsAndCaptureLast().to());

		Clock.setForCurrentThread(Clock.currentTimeMillis()+ONE_DAY);
		assertNotNull(sendEmailsAndCaptureLast().to());
	}

	@Test
	public void unsubscribe() throws Refusal, IOException {
		User leo = signup("leo");
		createEvent(leo, "eventToday", startTime+11L);
		createEvent(leo, "eventTomorrow", startTime+11L+ONE_DAY);

		Clock.setForCurrentThread(startTime);
		assertNotNull(sendEmailsAndCaptureLast().to());

		leo.setUnsubscribedFromEmails(true);

		Clock.setForCurrentThread(Clock.currentTimeMillis()+ONE_DAY);
		assertNull(sendEmailsAndCaptureLast().to());
	}


	private long fourOClockOnWednesday() {
		return date(8, 16, 0);
	}


	private long date(int dayOfMonth, int hourOfDay, int minute) {
		return new GregorianCalendar(2012, Calendar.FEBRUARY, dayOfMonth, hourOfDay, minute).getTimeInMillis();
	}

	private EmailSenderMock sendEmailsAndCaptureLast() {
		EmailSenderMock emailSenderMock = new EmailSenderMock();
		ReminderMailer daemon = new ReminderMailer(agitter, emailSenderMock);
		daemon.sendEventRemindersIfNecessary();
		return emailSenderMock;
	}

	private User signup(String username) throws Refusal {
		return agitter.users().signup(email(username+"@email.com"), "123");
	}
}

