package agitter.server.domain;


import java.io.Serializable;
import java.util.HashMap;

import agitter.shared.Mail;

class ScheduledMails implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer _scheduledMailsCount = 0;
	private HashMap<String, Mail> _scheduledMails = new HashMap<String, Mail>();

	void scheduleMail(Mail mail) {
		_scheduledMailsCount++;
		_scheduledMails.put(_scheduledMailsCount.toString(), mail);
	}

	HashMap<String, Mail> getScheduledMails() {
		return _scheduledMails;
	}

	void deleteMail(String key) {
		_scheduledMails.remove(key);
	}
}
