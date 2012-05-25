package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;

import java.util.logging.Level;

import agitter.domain.Agitter;

public class ReminderMailer {

	public static void start(Agitter agitter, EmailSender sender) {
		final ReminderMailer instance = new ReminderMailer(agitter, sender);
		new Thread() { { setDaemon(true); } @Override public void run() {
			while(true) {
				instance.sendEventRemindersIfNecessary();
				instance.sleepAMinute();
			}
		}}.start();
	}

	
	private final EmailSender sender;
	private final Agitter agitter;

	public ReminderMailer(Agitter agitter, EmailSender sender) {
		this.agitter = agitter;
		this.sender = sender;
	}


	public void sendEventRemindersIfNecessary() {
		if (!agitter.mailing().shouldSendScheduleNow()) return;
		agitter.mailing().markScheduleSent();

		new MailingRound(agitter.events(), sender).sendRemindersTo(agitter.users().all());
	}

	
	private void sleepAMinute() {
		try {
			Thread.sleep(60*1000);
		} catch(InterruptedException e) {
			getLogger(this).log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}
