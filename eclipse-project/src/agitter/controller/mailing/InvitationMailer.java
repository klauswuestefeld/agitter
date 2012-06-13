package agitter.controller.mailing;

import static infra.logging.LogInfra.getLogger;
import infra.logging.LogInfra;

import java.util.logging.Level;

import agitter.domain.events.Events;
import agitter.domain.events.Events.InvitationToSendOut;

public class InvitationMailer {

	public static void start(Events events, EmailSender sender) {
		final InvitationMailer instance = new InvitationMailer(events, sender);
		new Thread() { { setDaemon(true); } @Override public void run() {
			while(true) {
				instance.sendEventInvitationIfNecessary();
				instance.sleepAFewSeconds();
			}
		}}.start();
	}

	
	private final InvitationMailFormatter formatter = new InvitationMailFormatter();
	private final EmailSender sender;
	private final Events events;

	public InvitationMailer(Events events, EmailSender sender) {
		this.events = events;
		this.sender = sender;
	}


	public void sendEventInvitationIfNecessary() {
		InvitationToSendOut inv = events.popInvitationToSendOut();
		if (inv == null) return;

		try {
			tryToSendInvitation(inv);
		} catch (Exception e) {
			LogInfra.getLogger(this).log(Level.SEVERE, "Exception trying to send mail to " + inv.invitee().email(), e);
		}
	}


	private void tryToSendInvitation(InvitationToSendOut inv) {
		sender.send(inv.invitee().email(), formatter.subjectFor(inv.event()), formatter.bodyFor(inv.event(), inv.invitee()));
	}

	
	private void sleepAFewSeconds() {
		try {
			Thread.sleep(1*1000); // One second to stress the server. 5 seconds latency in the future is OK.
		} catch(InterruptedException e) {
			getLogger(this).log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}
