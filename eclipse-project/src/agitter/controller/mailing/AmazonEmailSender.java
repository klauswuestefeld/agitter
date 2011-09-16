package agitter.controller.mailing;

import java.io.IOException;
import java.util.Collections;

import agitter.domain.emails.EmailAddress;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class AmazonEmailSender implements EmailSender {

	private static final String FROM = "Agitter<no-reply@agitter.com>";

	private final AmazonSimpleEmailService service;

	public AmazonEmailSender() throws IOException {
		service = new AmazonSimpleEmailServiceClient(new PropertiesCredentials(EventsMailFormatter.class.getResourceAsStream("AwsCredentials.properties")));
	}

	@Override
	public void send(EmailAddress to, String subject, String body) {
		Destination destination = new Destination(Collections.singletonList(to.toString()));
		Content contentSubject = new Content(subject);
		Body contentBody = new Body().withHtml(new Content(body));
		Message message = new Message(contentSubject, contentBody);
		service.sendEmail(new SendEmailRequest(FROM, destination, message)); //TODO - Check send results
	}

}
