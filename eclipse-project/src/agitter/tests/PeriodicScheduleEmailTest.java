package agitter.tests;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.PeriodicScheduleMailer;
import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import agitter.domain.users.User;
import agitter.mailing.tests.EmailSenderMock;

public class PeriodicScheduleEmailTest extends CleanTestBase {

	private static final long ONE_DAY = 1000 * 60 * 60 * 24;
	private final Agitter agitter = new AgitterImpl();

	
	@Test
	public void sendingEmailsForNext24Hours() throws Refusal, IOException {
		long fourOClockToday = new GregorianCalendar(2011, Calendar.APRIL, 1, 16, 0).getTimeInMillis();
		Clock.setForCurrentThread(fourOClockToday);
		
		User leo = signup("leo");
		User klaus = signup("klaus");

		agitter.events().create(klaus, "event1", fourOClockToday + 10L);
		agitter.events().create(klaus, "event2", fourOClockToday + 11L);
		agitter.events().create(leo, "churras", fourOClockToday + 11L);
		agitter.events().create(klaus, "event3", fourOClockToday + 12L);
		agitter.events().create(klaus, "event4", fourOClockToday + 13L);
		agitter.events().create(klaus, "eventNextDay", fourOClockToday + 13L + ONE_DAY);

		Clock.setForCurrentThread(fourOClockToday + 11);

		EmailSenderMock mock = sendEmailsAndCaptureLast();

		assertEquals("klaus@email.com", mock.to());
		assertEquals("Agitos da Semana", mock.subject());
		final String body = "Olá klaus, seus amigos estão agitando e querem você lá: <br/><BR><BR>leo - churras<BR><BR>klaus - event2<BR><BR>klaus - event3<BR><BR>klaus - event4<BR><a href=\"http://agitter.com\">Acesse o Agitter</a> para ficar por dentro e também convidar seus amigos para festas, eventos, saídas ou qualquer outro tipo de agito.<BR><BR><BR>Saia da Internet. Agite!   \\o/<BR>Equipe Agitter<BR><a href=\"http://agitter.com\">agitter.com</a><BR/>";
		assertEquals(body, mock.body());
	}

	
	@Test
	public void onlyOnceADay() throws Refusal, IOException {
		User leo = signup("leo");
		agitter.events().create(leo, "eventToday", 11L);
		agitter.events().create(leo, "eventTomorrow", 11L + ONE_DAY);
		
		assertNotNull(sendEmailsAndCaptureLast().to());
		assertNull(sendEmailsAndCaptureLast().to());
		
		Clock.setForCurrentThread(Clock.currentTimeMillis() + ONE_DAY);
		assertNotNull(sendEmailsAndCaptureLast().to());
	}


	private EmailSenderMock sendEmailsAndCaptureLast() {
		EmailSenderMock emailSenderMock = new EmailSenderMock();
		PeriodicScheduleMailer daemon = new PeriodicScheduleMailer(agitter, emailSenderMock);
		daemon.sendEventsToHappenIn24Hours();
		return emailSenderMock;
	}

	
	private User signup(String username) throws Refusal {
		return agitter.users().signup(username, username + "@email.com", "123");
	}
}

