package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;
import static java.util.Calendar.SATURDAY;

import java.util.List;
import java.util.logging.Level;

import agitter.domain.events.EventOcurrence;
import utils.DateUtils;
import agitter.domain.events.Events;
import agitter.domain.users.User;

public class ReminderMailingRound {

	private static final String SUBJECT = "Lembretes de Agito";

	private final EmailSender _sender;
	private final ReminderMailFormatter _formatter = new ReminderMailFormatter();
	private final EventChooserForReminders _chooser;


	ReminderMailingRound(Events events, EmailSender sender) {
		_chooser = new EventChooserForReminders(events);
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
		List<EventOcurrence> toSend = _chooser.choose(user);
		if (toSend.isEmpty()) return;
		getLogger(this).info("Sending events to user: " + user);
		sendReminderTo(user, toSend);
	}


	private void sendReminderTo(User u, List<EventOcurrence> toSend) {
		String body = this._formatter.bodyFor(u, toSend);
		this._sender.send(u.email(), SUBJECT, body);
	}
	
}
