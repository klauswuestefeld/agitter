package agitter.tests;

import java.io.IOException;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.PeriodicScheduleNotificationDaemon;
import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import agitter.domain.users.User;
import agitter.email.tests.EmailSenderMock;

public class PeriodicScheduleEmailTest extends CleanTestBase {

	@Test
	public void periodicSchedule() throws Refusal, IOException {
		Clock.setForCurrentThread(9);
		Agitter agitter = new AgitterImpl();
		User leo = agitter.users().signup("leo", "leo@email.com", "pass");
		User klaus = agitter.users().signup("klaus", "klaus@email.com", "pass");


		agitter.events().create(klaus, "event1", 10L);
		agitter.events().create(klaus, "event2", 11L);
		agitter.events().create(leo, "churras", 11L);
		agitter.events().create(klaus, "event3", 12L);
		agitter.events().create(klaus, "event4", 13L);

		Clock.setForCurrentThread(11);

		EmailSenderMock emailSenderMock = new EmailSenderMock();
		PeriodicScheduleNotificationDaemon daemon = new PeriodicScheduleNotificationDaemon(agitter, emailSenderMock);
		daemon.sendEventsToHappenIn24Hours();

		assertEquals("klaus@email.com", emailSenderMock.to());
		assertEquals("Agitos da Semana", emailSenderMock.subject());
		final String body = "Olá klaus, seus amigos estão agitando e querem você lá: <br/><BR><BR>leo - churras<BR><BR>klaus - event2<BR><BR>klaus - event3<BR><BR>klaus - event4<BR><a href=\"http://agitter.com\">Acesse o Agitter</a> para ficar por dentro e também convidar seus amigos para festas, eventos, saídas ou qualquer outro tipo de agito.<BR><BR><BR>Saia da Internet. Agite!   \\o/<BR>Equipe Agitter<BR><a href=\"http://agitter.com\">agitter.com</a><BR/>";
		assertEquals(body, emailSenderMock.body());

	}
}

