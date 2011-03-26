package guardachuva.agitos.server.mailer;

import static org.apache.commons.logging.LogFactory.getLog;
import guardachuva.agitos.server.domain.ApplicationImpl;
import guardachuva.agitos.server.mailer.core.Mailer;
import guardachuva.agitos.server.mailer.core.MailerException;
import guardachuva.agitos.server.mailer.templates.MailTemplate;
import guardachuva.agitos.shared.Application;
import guardachuva.agitos.shared.Mail;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;

public class MailerServer {

	private final static String FROM_MAIL = "no-reply@vagaloom.com";
	private final static String FROM_NAME = "Vagaloom";
	private final static String SUBJECT_PREFIX = "[Vagaloom] ";

	
	private final Mailer _mailer;
	private Application _application;
	Log _log = getLog(MailerServer.class);
	private Timer timer = new Timer();

	private MailerServer(Application application) throws MailerException {
		_application = application;
		
		_log.info("Starting mailer(mail.server=" + getMailServerHost()+ ")");
		
		_application = ApplicationImpl.GetInstance();

		_mailer = new Mailer(getMailServerHost(), FROM_MAIL, FROM_NAME);
	}

	private String getMailServerHost() {
		return System.getProperty("mail.server", "192.168.1.51");
	}
	
	private void startTimer() {
		timer.scheduleAtFixedRate(new TimerTask() { @Override public void run() {
			process();			
			}}, 0, 2000);
	}

	private void process() {
		tryToProcess();
	}
	
	private void tryToProcess() {
		HashMap<String, Mail> mails = _application.getScheduledMails();
		for (String key : mails.keySet())
			send(key, mails.get(key));
	}

	private void send(final String key, final Mail mail) {
		try {
			_log.debug("Enviando...");
			send(mail);
			_log.debug("Enviado. Confirmando....");
			markSent(key);
			_log.debug("Confirmado.");
		} catch (MailerException e) {
			_log.info("Error Enviando email. " + e.getMessage());			
		}
	}

	private void send(Mail mail) throws MailerException {
		MailTemplate template = getTemplate(mail.getTemplate());
		_mailer.send(mail.getToMail(), mail.getToMail(),
				SUBJECT_PREFIX + template.getSubject(),
				template.fillOutWith(mail)
		);
	}
	
	private void markSent(String key) {
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

	public static void startRunning(Application application) throws MailerException {
		new MailerServer(application).startTimer();
	}

}
