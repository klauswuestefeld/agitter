package agitter.mailing;


import java.io.IOException;
import java.util.Arrays;

import agitter.domain.emails.EmailAddress;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;

//TODO: Please, refactor all this!
public class ActivationMailDispatcher {

	public static boolean isTESTMODE = false; //TODO - Implementar test support

	private static final String FROM = "no-reply@agitter.com";
	private static final String FROM_NAME = "Agitter";
	private static final String SUBJECT = "Ativação de conta no Agitter";
	private static final String BODY = "Bem vindo ao Agitter. Para ativar sua conta clique no link abaixo:<br/><br/>"
		+ "<a href=\"http://agitter.com/activation/?email=%EMAIL%&code=%CODE%\">http://agitter.com/activation/?email=%EMAIL%&code=%CODE%</a><br/><br/><br/>"
		+ "<a href=\"http://agitter.com\">Agitter</a><br /><br />"
		+ "Bons agitos,<br />Equipe Agitter.";

	public static void send(EmailAddress emailTo, String activationCode) throws IOException {
		if(isTESTMODE) return;

		PropertiesCredentials credentials = new PropertiesCredentials(
				ActivationMailDispatcher.class
						.getResourceAsStream("AwsCredentials.properties"));
		
		AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient(credentials);		

		Destination destination = new Destination(Arrays.asList(emailTo.toString()));
		Content subject = new Content(SUBJECT);
		String mailText = BODY.replaceAll("%EMAIL%", emailTo.toString()).replaceAll("%CODE%", activationCode);
		Body body = new Body().withHtml(new Content(mailText));
		Message message = new Message(subject, body);
		service.sendEmail(new SendEmailRequest(from(), destination, message));
	}		
	
	private static String from() {
		return FROM_NAME + "<" + FROM + ">";
	}

}
