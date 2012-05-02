package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;
import static java.util.Calendar.SATURDAY;

import java.util.List;
import java.util.logging.Level;

import utils.DateUtils;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;

public class MailingRound {

	private static final String SUBJECT = "Lembretes de Agito";

	private final EmailSender _sender;
	private final Events _events;

	private final EventsMailFormatter _formatter = new EventsMailFormatter();

	private final EventChooser _chooser = new EventChooser();


	MailingRound(Events events, EmailSender sender) {
		this._events = events;
		this._sender = sender;
	}


	void sendRemindersTo(Iterable<User> users) {
		if (DateUtils.isToday(SATURDAY)) return;

		for (User user : users)
			sendRemindersTo(user);
	}


	private void sendRemindersTo(User user) {
		try {
			tryToSendRemindersTo(user);
		} catch(RuntimeException e) {
			getLogger(this).log(Level.SEVERE, "Error sending email to: " + user+ "/" + user.email() + " - " + e.getMessage(), e);
		}
	}


	private void tryToSendRemindersTo(User user) {
		if (user.hasUnsubscribedFromEmails()) return;
		List<Event> candidateEvents = this._events.toHappen(user);
		List<Event> toSend = _chooser.choose(candidateEvents);
		if (toSend.isEmpty()) return;
		getLogger(this).info("Sending events to user: " + user);
		sendReminderTo(user, toSend);
	}


	private void sendReminderTo(User u, List<Event> toSend) {
		String body = this._formatter.format(u, toSend);
		this._sender.send(u.email(), SUBJECT, body);
	}
	
}
