package agitter.controller.mailing;

import static utils.XssAttackSanitizer.ultraConservativeFilter;
import sneer.foundation.lang.Clock;
import agitter.controller.AuthenticationToken;
import agitter.domain.events.Event;
import agitter.domain.users.User;
import agitter.ui.helper.HTMLFormatter;

public class InvitationMailFormatter {

	private static final String BODY = "%DESC%"
			+"<BR/><BR/><BR/>"
			+"<a href=\"http://agitter.com/%AUTH%\">Acesse o Agitter</a> para ficar por dentro e convidar seus amigos para festas, encontros, espetáculos ou qualquer tipo de agito."
			+"<BR/><BR/>Saia da Internet. Agite!   \\o/"
			+"<BR/><a href=\"http://agitter.com/%AUTH%\">agitter.com</a><BR/>";
	//			+"<BR><BR>Para não receber mais nenhum convite clique: <a href=\"http://agitter.com\">unsubscribe</a>";

	private static final long TWO_DAYS = 1000 * 60 * 60 * 24 * 2;
	private static final HTMLFormatter HTML_FORMATTER = new HTMLFormatter();


	public String subjectFor(Event event) {
		String desc = event.description();
		String ret = desc.substring(0, Math.min(desc.length(), 50));
		return ultraConservativeFilter(ret);
	}
	
	
	public String bodyFor(Event event, User user) {
		return BODY
			.replaceAll("%DESC%", HTML_FORMATTER.makeClickableWithBr(event.description()))
			.replaceAll("%AUTH%", authUri(user));
	}

	
	private String authUri(User u) {
		return new AuthenticationToken(u.email(), Clock.currentTimeMillis() + TWO_DAYS).asSecureURI();
	}

}
