package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import sneer.foundation.lang.Clock;
import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.users.User;

public class PeriodicScheduleMailer {

	private static final int MAX_EVENTS_TO_SEND = 5;
	private static final String SUBJECT = "Agitos da Semana";

	public static void start(Agitter agitter, EmailSender sender) {
		final PeriodicScheduleMailer instance = new PeriodicScheduleMailer(agitter, sender);
		new Thread() {
			{setDaemon(true); }
			@Override
			public void run() {
				while(true) {
					instance.sendEventsToHappenTomorrow();
					instance.sleepHalfAnHour();
				}
			}
		}.start();
	}

	private void sleepHalfAnHour() {
		try {
			Thread.sleep(30*60*1000);
		} catch(InterruptedException e) {
			getLogger(this).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private final EmailSender sender;
	private final Agitter agitter;

	private EventsMailFormatter formatter = new EventsMailFormatter();

	public PeriodicScheduleMailer(Agitter agitter, EmailSender sender) {
		this.sender = sender;
		this.agitter = agitter;
	}


	public void sendEventsToHappenTomorrow() {
		if(!agitter.mailing().shouldSendScheduleNow()) { return; }
		agitter.mailing().markScheduleSent();

		for(User user : agitter.users().all()) { sendEventsToHappenTomorrow(user); }
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
		if(!user.isSubscribedToEmails()) return;
		List<Event> candidateEvents = agitter.events().toHappen(user);
		List<Event> toSend = choose(candidateEvents);
		if(toSend.isEmpty()) return;
		sendTo(user, toSend);
	}

	private boolean isTimeToSendMail(Event e) {
		GregorianCalendar cal = zeroHourToday();
		cal.add(Calendar.DATE, 1);
		long tomorrow = cal.getTimeInMillis();
		cal.add(Calendar.DATE, 1);
		long afterTomorrow = cal.getTimeInMillis();
		
		for (long datetime : e.datetimes()) 
			if (datetime >= tomorrow && datetime < afterTomorrow)
				return true;
		
		return false;
	}

	private GregorianCalendar zeroHourToday() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(Clock.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
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

}
