package guardachuva.mailer.core;

import java.util.HashMap;

public class ScheduledMailsImpl implements ScheduledEmails {

	private Integer _scheduledMailsCount = 0;
	private HashMap<String, Mail> _scheduledMails = new HashMap<String, Mail>();

	/* (non-Javadoc)
	 * @see guardachuva.mailer.core.ScheduledEmails#scheduleMail(guardachuva.mailer.core.Mail)
	 */
	public void scheduleMail(Mail mail) {
		_scheduledMailsCount++;
		_scheduledMails.put(_scheduledMailsCount.toString(), mail);
	}

	/* (non-Javadoc)
	 * @see guardachuva.mailer.core.ScheduledEmails#getScheduledMails()
	 */
	public HashMap<String, Mail> getScheduledMails() {
		return _scheduledMails;
	}

	/* (non-Javadoc)
	 * @see guardachuva.mailer.core.ScheduledEmails#deleteMail(java.lang.String)
	 */
	public void deleteMail(String key) {
		_scheduledMails.remove(key);
	}
}
