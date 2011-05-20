package agitter.mailing;

import java.util.List;

import agitter.domain.events.Event;

public class EventsMailFormatter {

	private static final String BODY = "Olá %USERNAME%, seus amigos estão agitando e querem você lá: <br/><br/>"
			+"%EVENT_LIST%"
			+"<BR/><BR/><BR/>"
			+"<a href=\"http://agitter.com\">Acesse o Agitter</a> para ficar por dentro e convidar seus amigos para festas, encontros, baladas ou qualquer tipo de agito."
			+"<BR/><BR/>Saia da Internet. Agite!   \\o/"
			+"<BR/><a href=\"http://agitter.com\">agitter.com</a><BR/>";
	//			+"<BR><BR>Para não receber mais nenhum convite clique: <a href=\"http://agitter.com\">unsubscribe</a>";


	public String format(String username, List<Event> events) {
		String agitos = eventList(events);
		return BODY.replaceAll("%USERNAME%", username).replaceAll("%EVENT_LIST%", agitos);
	}

	private String eventList(List<Event> events) {
		StringBuffer result = new StringBuffer();
		for(Event e : events) {
			if (result.length() != 0)
				result.append("<BR/><BR/>");
			result.append(e.owner().fullName());
			result.append(" - ");
			result.append(e.description());
		}
		return result.toString();
	}
}
