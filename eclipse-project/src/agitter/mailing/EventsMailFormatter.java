package agitter.mailing;

import java.util.List;

import agitter.domain.events.Event;

public class EventsMailFormatter {

	private static final String BODY = "Olá %USERNAME%, seus amigos estão agitando e querem você lá: <br/>"
			+"%AGITOS%"
			+"<BR>"
			+"<a href=\"http://agitter.com\">Acesse o Agitter</a> para ficar por dentro e também convidar seus amigos para festas, eventos, saídas ou qualquer outro tipo de agito."
			+"<BR><BR><BR>Saia da Internet. Agite!   \\o/"
			+"<BR>Equipe Agitter"
			+"<BR><a href=\"http://agitter.com\">agitter.com</a><BR/>";
	//			+"<BR><BR>Para não receber mais nenhum convite clique: <a href=\"http://agitter.com\">unsubscribe</a>";


	public String format(String username, List<Event> events) {
		String agitos = mensagemAgitos(events);
		return BODY.replaceAll("%USERNAME%", username).replaceAll("%AGITOS%", agitos);
	}

	private String mensagemAgitos(List<Event> events) {
		StringBuffer strB = new StringBuffer();
		for(Event e : events) {
			strB.append("<BR><BR>");
			strB.append(e.owner().fullName());
			strB.append(" - ");
			strB.append(e.description());
		}
		return strB.toString();
	}
}
