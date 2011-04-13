package agitter.shared;


import java.util.HashMap;

public interface ScheduledEmails {

	public abstract void scheduleMail(Mail mail);

	public abstract HashMap<String, Mail> getScheduledMails();

	public abstract void deleteMail(String key);

}