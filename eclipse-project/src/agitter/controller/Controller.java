package agitter.controller;

import infra.logging.LogInfra;

import java.io.IOException;
import java.util.logging.Level;

import agitter.controller.mailing.AmazonEmailSender;
import agitter.controller.mailing.EmailSender;
import agitter.controller.mailing.PeriodicScheduleMailer;
import agitter.controller.mailing.SignupEmailController;
import agitter.domain.Agitter;
import agitter.main.PrevaylerBootstrap;

public class Controller {

	public static final Controller CONTROLLER = new Controller();
	private Controller() {	}
	
	private final Agitter domain = PrevaylerBootstrap.agitter();
	private final EmailSender emailSender = initEmailing();
	private final SignupEmailController signups = new SignupEmailController(emailSender, domain.users());
	
	
	public Agitter domain() {
		return domain;
	}
	
	
	public SignupEmailController signups() {
		return signups;
	}
	
	
	public EmailSender emailSender() {
		return emailSender;
	}

	
	private EmailSender initEmailing() {
		try {
			AmazonEmailSender sender = new AmazonEmailSender();
			PeriodicScheduleMailer.start(domain, sender);
			return sender;
		} catch (IOException e) {
			log(e, "Mailing start failed.");
			return null;
		}
	}

	
	private void log(Exception e, String message) {
		LogInfra.getLogger(this).log(Level.SEVERE, message, e);
	}

}
