package agitter.mailing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.users.User;
import sneer.foundation.lang.Clock;

public class PeriodicScheduleMailer {

	private static final int MAX_EVENTS_TO_SEND = 5;
	private static final String SUBJECT = "Agitos da Semana";

	public static void start(Agitter agitter, AmazonEmailSender amazonEmailSender) {
		final PeriodicScheduleMailer instance = new PeriodicScheduleMailer(agitter, amazonEmailSender);
		new Thread() {
			{setDaemon(true); }
			@Override
			public void run() {
				while(true) {
					instance.sendEventsToHappenIn24Hours();
					sleepHalfAnHour();
				}
			}
		}.start();
	}

	private static Logger getLogger() {return Logger.getLogger("agitter.mailing");}
	private static void sleepHalfAnHour() {
		try {
			Thread.sleep(30*60*1000);
		} catch(InterruptedException e) {
			getLogger().log(Level.SEVERE, e.getMessage(), e);
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
//			getLogger().info("Sending events to user: " + user.username()); //TODO - Info or Fine ?
			tryToSendEventsToHappenIn24Hours(user);
		} catch(RuntimeException e) {
			getLogger().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void tryToSendEventsToHappenIn24Hours(User user) {
		if(!user.isInterestedInPublicEvents()) { return; }
		List<Event> candidateEvents = agitter.events().toHappen(user);
		List<Event> toSend = choose(candidateEvents);
		if(toSend.isEmpty()) { return; }
		sendTo(user, toSend);
	}


	private List<Event> choose(List<Event> candidates) {
		final long dateLimit = dateLimit();
		List<Event> result = new ArrayList<Event>(MAX_EVENTS_TO_SEND);
		for(Event e : candidates) {
			if(e.datetime() > dateLimit) { break; }
			if(result.size()==MAX_EVENTS_TO_SEND) { break; }
			result.add(e);
		}
		return result;
	}

	private long dateLimit() {
		return Clock.currentTimeMillis()+24*60*60*1000;
	}

	private void sendTo(User u, List<Event> toSend) {
		String body = formatter.format(u.username(), toSend);
		sender.send(u.email(), SUBJECT, body);
	}

}
