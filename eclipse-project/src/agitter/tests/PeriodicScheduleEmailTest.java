package agitter.tests;

import java.io.IOException;

import agitter.PeriodicScheduleNotificationDaemon;
import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import agitter.domain.events.Event;
import agitter.domain.users.User;
import agitter.email.tests.EmailSenderMock;
import org.junit.Test;
import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;

@SuppressWarnings({"MagicNumber"})
public class PeriodicScheduleEmailTest extends CleanTestBase {

	@Test
	public void periodicSchedule() throws Refusal, IOException {
		Clock.setForCurrentThread(9);
		Agitter agitter = new AgitterImpl();
		User guest = agitter.users().signup("leo", "leo@email.com", "pass");
		User host = agitter.users().signup("klaus", "klaus@email.com", "pass");


		Event e1 = agitter.events().create(host, "e1", 10L);
		Event e2 = agitter.events().create(host, "e2", 11L);
		Event e3 = agitter.events().create(host, "e3", 12L);
		Event e4 = agitter.events().create(host, "e4", 13L);

		Clock.setForCurrentThread(11);

		EmailSenderMock emailSenderMock = new EmailSenderMock();
		PeriodicScheduleNotificationDaemon daemon = new PeriodicScheduleNotificationDaemon(agitter, emailSenderMock);
		daemon.sendEventsToHappenIn24Hours();

		assertEquals("leo@email.com", emailSenderMock.to());
		assertEquals("Agitos da Semana", emailSenderMock.subject());
		final String body = "Olá leo, seus amigos estão agitando e querem você lá: <br/><BR><BR>klaus - e2<BR><BR>klaus - e3<BR><BR>klaus - e4<BR><a href=\"http://agitter.com\">Acesse o Agitter</a> para ficar por dentro e também convidar seus amigos para festas, eventos, saídas ou qualquer outro tipo de agito.<BR><BR><BR>Saia da Internet. Agite!   \\o/<BR>Equipe Agitter<BR><a href=\"http://agitter.com\">agitter.com</a><BR/>";
		assertEquals(body, emailSenderMock.body());

	}
}

