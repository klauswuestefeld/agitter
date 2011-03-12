package guardachuva.mailer.core;

import java.util.HashMap;

public class ScheduledMails {

	private Integer _scheduledMailsCount = 0;
	private HashMap<String, Mail> _scheduledMails = new HashMap<String, Mail>();

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
}
