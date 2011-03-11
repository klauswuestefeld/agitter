package guardachuva.agitos.server.application;

import guardachuva.agitos.server.application.homes.EventHome;
import guardachuva.agitos.server.application.homes.UserHome;
import guardachuva.mailer.core.Mail;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class Application implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public UserHome _userHome = new UserHome();
	public EventHome _eventHome = new EventHome();
	
	private static SimpleDateFormat _dateFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private Integer _scheduledMailsCount = 0;
	private HashMap<String, Mail> _scheduledMails = new HashMap<String, Mail>();


	public UserHome userHome() {
		return _userHome;
	}

	public void scheduleMail(Mail mail) {
		_scheduledMailsCount++;
		_scheduledMails.put(_scheduledMailsCount.toString(), mail);
	}

	public HashMap<String, Mail> getScheduledMails() {
		return _scheduledMails;
	}

	public void deleteMail(String key) {
		_scheduledMails.remove(key);
	}

	public static Date strToDate(String string) throws ParseException {
		return _dateFormater.parse(string);
	}

}

