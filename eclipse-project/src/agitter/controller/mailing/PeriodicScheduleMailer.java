package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;

import java.util.logging.Level;

import agitter.domain.Agitter;

public class PeriodicScheduleMailer {

	public static void start(Agitter agitter, EmailSender sender) {
		final PeriodicScheduleMailer instance = new PeriodicScheduleMailer(agitter, sender);
		new Thread() { { setDaemon(true); } @Override public void run() {
			while(true) {
				instance.sendEventsToHappenTomorrow();
				instance.sleepHalfAnHour();
			}
		}}.start();
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

		new MailingRound(agitter.users().all(), agitter.events(), sender);
	}

}
