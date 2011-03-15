package guardachuva.mailer;

import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.shared.Mail;
import guardachuva.agitos.shared.rpc.RemoteApplication;
import guardachuva.mailer.core.Mailer;
import guardachuva.mailer.core.MailerException;
import guardachuva.mailer.templates.MailTemplate;

import java.io.IOException;
import java.util.HashMap;

import org.restlet.data.Form;

import sneer.bricks.hardware.clock.timer.Timer;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.exceptionhandling.ExceptionHandler;
import sneer.foundation.lang.ClosureX;

import com.gdevelop.gwt.syncrpc.SyncProxy;

public class MailerServer {

	private final static String FROM_MAIL = "no-reply@vagaloom.com";
	private final static String FROM_NAME = "Vagaloom";
	private final static String SUBJECT_PREFIX = "[Vagaloom] ";

	
	private final Mailer _mailer;

	@SuppressWarnings("unused")	private WeakContract _refToAvoidGc;
	private RemoteApplication _application;

	private MailerServer() throws MailerException {
		String agitosServer = System.getProperty("agitos.server", "127.0.0.1") +
			":" + System.getProperty("agitos.port", "80");
		String mailServer = System.getProperty("mail.server", "192.168.1.51");
		
		my(Logger.class).log("Starting mailer ({})", agitosServer);
		
		_mailer = new Mailer(mailServer, FROM_MAIL, FROM_NAME);
		_application = (RemoteApplication) SyncProxy.newProxyInstance(
				RemoteApplication.class, "http://127.0.0.1:8888/agitos/", "rpc");
	}
	
	private void startTimer() {
		_refToAvoidGc = my(Timer.class).wakeUpNowAndEvery(10000, new Runnable() { @Override public void run() {
			process();
		}});
	}

	private void process() {
		try {
			tryToProcess();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void tryToProcess() throws IOException {
		HashMap<String, Mail> mails = _application.getScheduledMails();
		for (String key : mails.keySet())
			send(key, mails.get(key));
	}

	private void send(final String key, final Mail mail) {
		my(ExceptionHandler.class).shieldX(new ClosureX<MailerException>() { @Override public void run() throws MailerException {
			my(Logger.class).log("Enviando...");
			send(mail);
			my(Logger.class).log("Enviado. Confirmando....");
			markSent(key);
			my(Logger.class).log("Confirmado.");
		}});
	}

	private void send(Mail mail) throws MailerException {
		MailTemplate template = getTemplate(mail.getTemplate());
		_mailer.send(mail.getToMail(), mail.getToMail(),
				SUBJECT_PREFIX + template.getSubject(),
				template.fillOutWith(mail)
		);
	}
	
	private void markSent(String key) {
		Form form = new Form();
		form.add("key", key);
		_application.deleteMail(key);
	}
	
	private MailTemplate getTemplate(String templateName) throws MailerException {
		try {
			MailTemplate template = (MailTemplate) Class.forName(templateName)
					.newInstance();
			return template;
		} catch (Exception ex) {
			throw new MailerException(ex);
		}
	}

	public static void startRunning() throws MailerException {
		new MailerServer().startTimer();
	}

}
