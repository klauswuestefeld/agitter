package agitter.controller.mailing;

import static utils.XssAttackSanitizer.ultraConservativeFilter;

import java.util.List;

import agitter.domain.events.EventOcurrence;
import agitter.domain.users.User;
import agitter.ui.helper.HTMLFormatter;

public class ReminderMailFormatter extends MailFormatter {

	private static final String BODY = "Seus amigos estão agitando e querem você lá: <br/><br/>"
			+"%EVENT_LIST%"
			+"<BR/><BR/><BR/>"
			+"<a href=\"http://agitter.com\">Acesse o Agitter</a> para ficar por dentro e convidar seus amigos para festas, encontros, espetáculos ou qualquer tipo de agito."
			+"<BR/><BR/>Saia da Internet. Agite!   \\o/"
			+"<BR/><a href=\"http://agitter.com/%AUTH%\">agitter.com</a><BR/>";
	//			+"<BR><BR>Para não receber mais nenhum convite clique: <a href=\"http://agitter.com\">unsubscribe</a>";


	public String bodyFor(User user, List<EventOcurrence> events) {
		return BODY
			.replaceAll("%EVENT_LIST%", eventList(events))
			.replaceAll("%AUTH%", authUri(user));
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
