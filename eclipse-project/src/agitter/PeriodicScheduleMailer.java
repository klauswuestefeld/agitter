package agitter;

import java.util.ArrayList;
import java.util.List;

import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.users.User;
import agitter.mailing.AmazonEmailSender;
import agitter.mailing.EmailSender;
import agitter.mailing.EventsMailFormatter;
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

	private static void sleepHalfAnHour() {
		try {
			Thread.sleep(30*60*1000);
		} catch(InterruptedException e) {
			e.printStackTrace();
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
