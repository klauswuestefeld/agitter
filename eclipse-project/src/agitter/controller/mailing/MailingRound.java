package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.SATURDAY;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import sneer.foundation.lang.Clock;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;

public class MailingRound {

	private static final int MAX_EVENTS_TO_SEND = 5;
	private static final String SUBJECT = "Agitos da Semana";

	private final EmailSender _sender;
	private final Events _events;

	private final EventsMailFormatter _formatter = new EventsMailFormatter();
	private final long _startTime = daysFromNow(1);
	private final long _endTime = endTime();


	MailingRound(Events events, EmailSender sender) {
		this._events = events;
		this._sender = sender;
	}


	void sendRemindersTo(Iterable<User> users) {
		if (isToday(SATURDAY)) return;

		for (User user : users)
			sendRemindersTo(user);
	}


	private void sendRemindersTo(User user) {
		try {
			getLogger(this).info("Sending events to user: " + user);
			tryToSendRemindersTo(user);
		} catch(RuntimeException e) {
			getLogger(this).log(Level.SEVERE, "Erro enviando email para: " + user+ "/" + user.email() + " - " + e.getMessage(), e);
		}
	}

	private void tryToSendRemindersTo(User user) {
		if (user.hasUnsubscribedFromEmails()) return;
		List<Event> candidateEvents = this._events.toHappen(user);
		List<Event> toSend = choose(candidateEvents);
		if (toSend.isEmpty()) return;
		sendReminderTo(user, toSend);
	}

	private boolean isTimeToRemindAbout(Event e) {
		for (long datetime : e.datetimes()) 
			if (datetime >= this._startTime && datetime < this._endTime)
				return true;
		
		return false;
	}

	private List<Event> choose(List<Event> candidates) {
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SEND);
		for(Event e : candidates) {
			if (result.size() == MAX_EVENTS_TO_SEND) break;
			if (!isTimeToRemindAbout(e)) continue;
			result.add(e);
		}
		return result;
	}


	private void sendReminderTo(User u, List<Event> toSend) {
		String body = this._formatter.format(toSend);
		this._sender.send(u.email(), SUBJECT, body);
	}

	
	private long endTime() {
		return isToday(FRIDAY)
			? daysFromNow(3)
			: daysFromNow(2);
	}


	private boolean isToday(int dayOfWeek) {
		return today().get(DAY_OF_WEEK) == dayOfWeek;
	}


	private static long daysFromNow(int days) {
		GregorianCalendar cal = today();
		cal.add(DATE, days);
		return cal.getTimeInMillis();
	}


	private static GregorianCalendar today() {
		GregorianCalendar cal = asCalendar(Clock.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}


	private static GregorianCalendar asCalendar(long millis) {
		GregorianCalendar ret = new GregorianCalendar();
		ret.setTimeInMillis(millis);
		return ret;
	}

}
