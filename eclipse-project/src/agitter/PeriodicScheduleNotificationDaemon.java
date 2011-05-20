package agitter;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.Clock;
import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.users.User;
import agitter.email.AmazonEmailSender;
import agitter.email.EmailSender;
import agitter.email.EventsMailFormatter;

public class PeriodicScheduleNotificationDaemon {

	private static final int MAX_EVENTS_TO_SEND = 5;
	private static final String SUBJECT = "Agitos da Semana";
	public static void start(Agitter agitter, AmazonEmailSender amazonEmailSender) {
		final PeriodicScheduleNotificationDaemon instance = new PeriodicScheduleNotificationDaemon(agitter, amazonEmailSender);
		new Thread() { {setDaemon(true); } @Override public void run() {
			while(true) {
				instance.sendEventsToHappenIn24Hours();
				sleepOneDay();
			}
		}}.start();
	}

	private static void sleepOneDay() {
		try {
			Thread.sleep(24*60*60*1000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	private final EmailSender sender;
	private final Agitter agitter;

	private EventsMailFormatter formatter = new EventsMailFormatter();

	public PeriodicScheduleNotificationDaemon(Agitter agitter, EmailSender sender) {
		this.sender = sender;
		this.agitter = agitter;
	}

	
	public void sendEventsToHappenIn24Hours() {
		for (User user : agitter.users().all())
			sendEventsToHappenIn24Hours(user);
	}

	
	private void sendEventsToHappenIn24Hours(User user) {
		List<Event> candidateEvents = agitter.events().toHappen(user);
		List<Event> toSend = choose(candidateEvents);
		if (toSend.isEmpty())
			return;
		sendTo(user, toSend);
	}

	
	private List<Event> choose(List<Event> candidates) {
		final long dateLimit = dateLimit();
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SEND);
		for(Event e : candidates) {
			if (e.datetime() > dateLimit) break;
			if (result.size() == MAX_EVENTS_TO_SEND) break;
			result.add(e);
		}
		return result;
	}

	
	private long dateLimit() {
		return Clock.currentTimeMillis() + 24*60*60*1000;
	}

	
	private void sendTo(User u, List<Event> toSend) {
		String body = formatter.format(u.username(), toSend);
		sender.send(u.email(), SUBJECT, body);
	}

}
