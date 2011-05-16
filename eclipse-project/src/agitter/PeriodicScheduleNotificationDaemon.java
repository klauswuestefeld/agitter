package agitter;

import java.util.ArrayList;
import java.util.List;

import agitter.domain.Agitter;
import agitter.domain.events.Event;
import agitter.domain.users.User;
import agitter.email.AmazonEmailSender;
import agitter.email.EmailSender;
import agitter.email.EventsMailFormatter;
import sneer.foundation.lang.Clock;

public class PeriodicScheduleNotificationDaemon {

	private static final String SUBJECT = "Agitos da Semana";
	public static void start(Agitter agitter, AmazonEmailSender amazonEmailSender) {
		final PeriodicScheduleNotificationDaemon daemon = new PeriodicScheduleNotificationDaemon(agitter, amazonEmailSender);
		new Thread() {
			{
				setDaemon(true);
				start();
			}

			public void run() {
				while(true) {
					daemon.sendEventsToHappenIn24Hours();
					try {
						Thread.sleep(24*60*60*1000);
					} catch(InterruptedException e) {
						e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
					}
				}
			}
		};
		//To change body of created methods use File | Settings | File Templates.
	}

	private final EmailSender sender;
	private final Agitter agitter;

	private EventsMailFormatter formatter = new EventsMailFormatter();

	public PeriodicScheduleNotificationDaemon(Agitter agitter, EmailSender sender) {
		this.sender = sender;
		this.agitter = agitter;
	}

	public void sendEventsToHappenIn24Hours() {
		for(User u : this.agitter.users().all()) {
			final long limit = limit();
			List<Event> events = agitter.events().toHappen(u);
			List<Event> toSend = new ArrayList<Event>();
			for(Event e : events) {
				if(e.datetime() > limit) { break; }
				if(!e.owner().equals(u)) { toSend.add(e); }
			}
			if(!toSend.isEmpty()) { sendTo(u, toSend); }
		}
	}
	private long limit() {
		return Clock.currentTimeMillis()+24*60*60*1000;
	}

	private void sendTo(User u, List<Event> toSend) {
		String body = formatter.format(u.username(), toSend);
		sender.send(u.email(), SUBJECT, body);
	}

}
