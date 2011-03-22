package guardachuva.agitos.server.mailer;

import guardachuva.agitos.server.mailer.core.Mailer;
import guardachuva.agitos.server.mailer.core.MailerException;


public class MailerSpike {
	
	public static void main(String[] args) {
	    String from_mail = "eu@agitos.com.br";
		String from_name = "Agitos Mailer";
	    String subject = "[Agitos] Link de Acesso";
	    String template = "http://home.webgoal.com.br:8080";
	    
		String to_mail = "altiereslopes@webgoal.com.br";
	    String to_name = "Você";
	    
		try {
			Mailer mailer = new Mailer("localhost", from_mail, from_name);
			mailer.send(to_mail, to_name, subject, template);
			System.out.println("email enviado!");
		} catch (MailerException e) {
			e.printStackTrace();
		}
	}
	
}
