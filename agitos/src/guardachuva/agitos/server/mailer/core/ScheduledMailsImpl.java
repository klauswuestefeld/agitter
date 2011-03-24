package guardachuva.agitos.server.mailer.core;

import guardachuva.agitos.shared.Mail;
import guardachuva.agitos.shared.ScheduledEmails;

import java.io.Serializable;
import java.util.HashMap;

public class ScheduledMailsImpl implements ScheduledEmails, Serializable {

	private static final long serialVersionUID = 1L;
	private Integer _scheduledMailsCount = 0;
	private HashMap<String, Mail> _scheduledMails = new HashMap<String, Mail>();

	/* (non-Javadoc)
	 * @see guardachuva.mailer.core.ScheduledEmails#scheduleMail(guardachuva.mailer.core.Mail)
	 */
	@Override
	public void scheduleMail(Mail mail) {
		_scheduledMailsCount++;
		_scheduledMails.put(_scheduledMailsCount.toString(), mail);
	}

	/* (non-Javadoc)
	 * @see guardachuva.mailer.core.ScheduledEmails#getScheduledMails()
	 */
	@Override
	public HashMap<String, Mail> getScheduledMails() {
		return _scheduledMails;
	}

	/* (non-Javadoc)
	 * @see guardachuva.mailer.core.ScheduledEmails#deleteMail(java.lang.String)
	 */
	@Override
	public void deleteMail(String key) {
		_scheduledMails.remove(key);
	}
}
