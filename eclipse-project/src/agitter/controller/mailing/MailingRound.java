package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;

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

	private final EmailSender sender;
	private final Events events;

	private final EventsMailFormatter formatter = new EventsMailFormatter();
	private final long tomorrow;
	private final long afterTomorrow;

	
	MailingRound(List<User> users, Events events, EmailSender sender) {
		this.events = events;
		this.sender = sender;
		
		tomorrow = daysFromNow(1);
		afterTomorrow = daysFromNow(2);
		
		for (User user : users)
			sendEventsToHappenTomorrow(user);
	}


	private void sendEventsToHappenTomorrow(User user) {
		try {
			getLogger(this).info("Sending events to user: " + user);
			tryToSendEventsToHappenTomorrow(user);
		} catch(RuntimeException e) {
			getLogger(this).log(Level.SEVERE, "Erro enviando email para: " + user+ "/" + user.email() + " - " + e.getMessage(), e);
		}
	}

	private void tryToSendEventsToHappenTomorrow(User user) {
		if (!user.isSubscribedToEmails()) return;
		List<Event> candidateEvents = events.toHappen(user);
		List<Event> toSend = choose(candidateEvents);
		if (toSend.isEmpty()) return;
		sendTo(user, toSend);
	}

	private boolean isTimeToSendMail(Event e) {
		for (long datetime : e.datetimes()) 
			if (datetime >= tomorrow && datetime < afterTomorrow)
				return true;
		
		return false;
	}

	private List<Event> choose(List<Event> candidates) {
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SEND);
		for(Event e : candidates) {
			if (result.size() == MAX_EVENTS_TO_SEND) break;
			if (!isTimeToSendMail(e)) continue;
			result.add(e);
		}
		return result;
	}


	private void sendTo(User u, List<Event> toSend) {
		String body = formatter.format(toSend);
		sender.send(u.email(), SUBJECT, body);
	}

	
	private static long daysFromNow(int days) {
		GregorianCalendar cal = zeroHourToday();
		cal.add(Calendar.DATE, days);
		return cal.getTimeInMillis();
	}


	private static GregorianCalendar zeroHourToday() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(Clock.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

}
