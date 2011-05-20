package agitter.tests;

import java.io.IOException;

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
		User leo = signup("leo");
		User klaus = signup("klaus");

		agitter.events().create(klaus, "event1", 10L);
		agitter.events().create(klaus, "event2", 11L);
		agitter.events().create(leo, "churras", 11L);
		agitter.events().create(klaus, "event3", 12L);
		agitter.events().create(klaus, "event4", 13L);
		agitter.events().create(klaus, "eventNextDay", 13L + ONE_DAY);

		Clock.setForCurrentThread(11);

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

