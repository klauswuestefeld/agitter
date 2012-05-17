package agitter.controller.mailing;

import static utils.XssAttackSanitizer.ultraConservativeFilter;

import java.util.List;

import agitter.domain.events.EventOcurrence;
import sneer.foundation.lang.Clock;
import agitter.controller.AuthenticationToken;
import agitter.domain.users.User;
import agitter.ui.helper.HTMLFormatter;

public class EventsMailFormatter {

	private static final String BODY = "Seus amigos estão agitando e querem você lá: <br/><br/>"
			+"%EVENT_LIST%"
			+"<BR/><BR/><BR/>"
			+"<a href=\"http://agitter.com\">Acesse o Agitter</a> para ficar por dentro e convidar seus amigos para festas, encontros, espetáculos ou qualquer tipo de agito."
			+"<BR/><BR/>Saia da Internet. Agite!   \\o/"
			+"<BR/><a href=\"http://agitter.com/%AUTH%\">agitter.com</a><BR/>";
	//			+"<BR><BR>Para não receber mais nenhum convite clique: <a href=\"http://agitter.com\">unsubscribe</a>";

	private static final long TWO_DAYS = 1000 * 60 * 60 * 24 * 2;


	public String format(User u, List<EventOcurrence> events) {
		return BODY
			.replaceAll("%EVENT_LIST%", eventList(events))
			.replaceAll("%AUTH%", authUri(u));
	}

	private String authUri(User u) {
		return new AuthenticationToken(u.email(), Clock.currentTimeMillis() + TWO_DAYS).asSecureURI();
	}

	private String eventList(List<EventOcurrence> events) {
		StringBuffer result = new StringBuffer();
		HTMLFormatter formatter = new HTMLFormatter();
		for(EventOcurrence occ : events) {
			if (result.length() != 0)
				result.append("<BR/><BR/>");
			result.append(ultraConservativeFilter(occ.event().owner().screenName()));
			result.append(" - ");
			//result.append(ultraConservativeFilter(e.description()));
			result.append(formatter.makeClickable(occ.event().description()));
		}
		return result.toString();
	}
}
