package agitter.controller;

import infra.logging.LogInfra;

import java.io.IOException;
import java.util.logging.Level;

import basis.lang.Functor;

import agitter.controller.mailing.AmazonEmailSender;
import agitter.controller.mailing.EmailSender;
import agitter.controller.mailing.InvitationMailer;
import agitter.controller.mailing.ReminderMailer;
import agitter.controller.mailing.SignupEmailController;
import agitter.controller.oauth.OAuth;
import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.main.PrevaylerBootstrap;

public class Controller {

	public static final Controller CONTROLLER = new Controller();
	private Controller() {	}
	
	private final Agitter domain = PrevaylerBootstrap.agitter();
	private final EmailSender emailSender = initEmailing();
	private final SignupEmailController signups = new SignupEmailController(emailSender, domain.users());
	private final OAuth oAuth = new OAuth(userProducer(), domain.contacts());
	
	
	public Agitter domain() {
		return domain;
	}
	
	
	public SignupEmailController signups() {
		return signups;
	}
	
	
	public EmailSender emailSender() {
		return emailSender;
	}


	public OAuth oAuth() {
		return oAuth;
	}


	private EmailSender initEmailing() {
		AmazonEmailSender sender;
		try {
			sender = new AmazonEmailSender();
		} catch (IOException e) {
			log(e, "Mailing start failed.");
			return null;
		}
		InvitationMailer.start(domain.events(), sender);
		ReminderMailer.start(domain, sender);
		return sender;
	}

	
	private void log(Exception e, String message) {
		LogInfra.getLogger(this).log(Level.SEVERE, message, e);
	}

	
	private Functor<EmailAddress, User> userProducer() {
		return UserUtils.userProducer(domain.users());
	}
}
