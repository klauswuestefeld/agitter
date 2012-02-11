package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import sneer.foundation.lang.Clock;
import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.users.User;

public class PeriodicScheduleMailer {
	private static final long TWO_HOURS = 1000 * 60 * 60 * 2;
	
	private static final int A_DAY_PLUS_TWO_HOURS = 24 + 2; //Two extra hours so that the events that are 24h and a few minutes from now get sent with enough notice time.
	private static final int MAX_EVENTS_TO_SEND = 5;
	private static final String SUBJECT = "Agitos da Semana";

	public static void start(Agitter agitter, EmailSender sender) {
		final PeriodicScheduleMailer instance = new PeriodicScheduleMailer(agitter, sender);
		new Thread() {
			{setDaemon(true); }
			@Override
			public void run() {
				while(true) {
					instance.sendEventsToHappenIn24Hours();
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


	public void sendEventsToHappenIn24Hours() {
		if(!agitter.mailing().shouldSendScheduleNow()) { return; }
		agitter.mailing().markScheduleSent();

		for(User user : agitter.users().all()) { sendEventsToHappenIn24Hours(user); }
	}


	private void sendEventsToHappenIn24Hours(User user) {
		try {
			getLogger(this).info("Sending events to user: " + user);
			tryToSendEventsToHappenIn24Hours(user);
		} catch(RuntimeException e) {
			getLogger(this).log(Level.SEVERE, "Erro enviando email para: " + user+ "/" + user.email() + " - " + e.getMessage(), e);
		}
	}

	private void tryToSendEventsToHappenIn24Hours(User user) {
		if(!user.isInterestedInPublicEvents()) { return; }
		List<Event> candidateEvents = agitter.events().toHappen(user);
		List<Event> toSend = choose(candidateEvents);
		if(toSend.isEmpty()) { return; }
		sendTo(user, toSend);
	}

	private boolean isTimeToSendMail(Event e, long dateLimit) {
		final long twoHoursAgo = Clock.currentTimeMillis() - TWO_HOURS;
		
		for (long datetime : e.datetimes()) { 
			if (datetime >= twoHoursAgo && datetime <= dateLimit)
				return true;
		}
		
		return false;
	}
	
	private List<Event> choose(List<Event> candidates) {
		final long dateLimit = dateLimit();
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SEND);
		for(Event e : candidates) {
			if (!isTimeToSendMail(e, dateLimit)) { continue; }
			if(result.size()==MAX_EVENTS_TO_SEND) { break; }
			result.add(e);
		}
		return result;
	}

	private long dateLimit() {
		return Clock.currentTimeMillis()+A_DAY_PLUS_TWO_HOURS*60*60*1000;
	}

	private void sendTo(User u, List<Event> toSend) {
		String body = formatter.format(toSend);
		sender.send(u.email(), SUBJECT, body);
	}

}
