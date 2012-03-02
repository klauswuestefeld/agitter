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

	
	private final EmailSender sender;
	private final Agitter agitter;

	public PeriodicScheduleMailer(Agitter agitter, EmailSender sender) {
		this.agitter = agitter;
		this.sender = sender;
	}


	public void sendEventsToHappenTomorrow() {
		if (!agitter.mailing().shouldSendScheduleNow()) return;
		agitter.mailing().markScheduleSent();

		new MailingRound(agitter.users().all(), agitter.events(), sender)
			.start();
	}

	
	private void sleepHalfAnHour() {
		try {
			Thread.sleep(30*60*1000);
		} catch(InterruptedException e) {
			getLogger(this).log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}
