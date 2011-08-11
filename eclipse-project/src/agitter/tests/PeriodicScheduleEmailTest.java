package agitter.tests;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.tests.EventsTestBase;
import agitter.domain.users.User;
import agitter.mailing.PeriodicScheduleMailer;
import agitter.mailing.tests.EmailSenderMock;

public class PeriodicScheduleEmailTest extends EventsTestBase {

	private static final long ONE_DAY = 1000*60*60*24;
	public final Agitter agitter = new AgitterImpl();
	private final long startTime = fourOClockToday();


	@Before
	public void beforeMailingTest() {
		Clock.setForCurrentThread(startTime);
	}


	@Test
	public void sendingEmailsForNext24Hours() throws Refusal, IOException {
		User leo = signup("leo");
		User klaus = signup("klaus");

		createEvent(klaus, "event1", startTime+10L);
		createEvent(klaus, "event2", startTime+11L);
		createEvent(leo, "churras", startTime+11L, new EmailAddress("klaus@email.com"));
		createEvent(klaus, "event3", startTime+12L);
		createEvent(klaus, "event4", startTime+13L);
		createEvent(klaus, "eventNextDay", startTime+13L+ONE_DAY);

		Clock.setForCurrentThread(startTime+11);

		EmailSenderMock mock = sendEmailsAndCaptureLast();

		assertEquals("klaus@email.com", mock.to());
		assertEquals("Agitos da Semana", mock.subject());
		final String body =
				"Olá klaus, seus amigos estão agitando e querem você lá: <br/><br/>leo - churras<BR/><BR/>klaus - event2<BR/><BR/>klaus - event3<BR/><BR/>klaus - event4<BR/><BR/><BR/><a href=\"http://agitter.com\">Acesse o Agitter</a> para ficar por dentro e convidar seus amigos para festas, encontros, espetáculos ou qualquer tipo de agito.<BR/><BR/>Saia da Internet. Agite!   \\o/<BR/><a href=\"http://agitter.com\">agitter.com</a><BR/>";
		assertEquals(body, mock.body());
	}

	@Test
	//@Ignore
	public void sendingEmailsToUnregisteredUsers() throws Refusal, IOException {
		User matias = signup("matias");
		EmailAddress klausEmail = new EmailAddress("klaus@email.com");
		createEvent(matias, "event1", startTime+10L);
		createEvent(matias, "churras", startTime+11L, klausEmail);
		createEvent(matias, "eventNextDay", startTime+13L+ONE_DAY, klausEmail);

		Clock.setForCurrentThread(startTime+10);

		EmailSenderMock mock = sendEmailsAndCaptureLast();

		assertEquals("klaus@email.com", mock.to());
		assertEquals("Agitos da Semana", mock.subject());
		assertFragment("Olá klaus@email.com", mock.body());
		assertFragment("matias - churras", mock.body());
	}
	
	@Test
	public void xssAttackFiltering() throws Refusal, IOException {
		User leo = signup("leo");

		createEvent(leo, "<script>", startTime+11L, new EmailAddress("fulano@email.com"));
		Clock.setForCurrentThread(startTime+11);

		EmailSenderMock mock = sendEmailsAndCaptureLast();
		assertFragment("leo - script", mock.body());
	}


	private void assertFragment(String expectedFragment, String actual) {
		assertTrue(actual.contains(expectedFragment));
	}

	
	@Override
	protected Event createEvent(User owner, String description, long startTime, EmailAddress... invitees) throws Refusal {
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

		leo.setInterestedInPublicEvents(false);

		Clock.setForCurrentThread(Clock.currentTimeMillis()+ONE_DAY);
		assertNull(sendEmailsAndCaptureLast().to());
	}


	private long fourOClockToday() {
		return new GregorianCalendar(2011, Calendar.APRIL, 1, 16, 0).getTimeInMillis();
	}

	private EmailSenderMock sendEmailsAndCaptureLast() {
		EmailSenderMock emailSenderMock = new EmailSenderMock();
		PeriodicScheduleMailer daemon = new PeriodicScheduleMailer(agitter, emailSenderMock);
		daemon.sendEventsToHappenIn24Hours();
		return emailSenderMock;
	}

	private User signup(String username) throws Refusal {
		return agitter.users().signup(username, username+"@email.com", "123");
	}
}

