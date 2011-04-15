package agitter.server.mailer.core;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

	private final MimeMessage _msg;

	public Mailer(String host, String fromMail, String fromName) throws MailerException {
		Properties p = new Properties();  
		p.put("mail.host", host);
		p.put("mail.smtp.timeout", 1000);
		p.put("mail.smtp.connectiontimeout", 500);
		Session session = Session.getInstance(p, null);  
		_msg = initMsg(fromMail, fromName, session);  
	}


	private MimeMessage initMsg(String fromMail, String fromName, Session session) throws MailerException {
		try {
			return tryToInitMsg(fromMail, fromName, session);
		} catch (Exception e) {
			throw new MailerException(e);
		}
	}


	private MimeMessage tryToInitMsg(String fromMail, String fromName, Session session) throws UnsupportedEncodingException, MessagingException {
		MimeMessage result = new MimeMessage(session);
		result.setFrom(new InternetAddress(fromMail, fromName));
		result.setSentDate(new Date());
		return result;
	}


	public void send(String to_mail, String to_name, String subject, String content) throws MailerException {
		try {
			_msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to_mail, to_name));  
			_msg.setSubject(subject);  
			_msg.setContent(content, "text/html");
			
			Transport.send(_msg);
		} catch (Exception e) {
			throw new MailerException(e);
		}
	}
	
}
