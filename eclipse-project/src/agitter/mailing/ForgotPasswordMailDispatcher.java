package agitter.mailing;

/*
 * Copyright 2011 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import java.io.IOException;
import java.util.Arrays;

import agitter.domain.emails.EmailAddress;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

//TODO: Please, refactor all this!
public class ForgotPasswordMailDispatcher {

	private static final String FROM = "no-reply@agitter.com";
	private static final String FROM_NAME = "Agitter";
	private static final String SUBJECT = "Lembrete de Senha";
	private static final String BODY = "Você solicitou que nós enviássemos sua senha.<br />"
		+ "Seus dados são:<br />"
		+ "Usuário: %USERNAME% <br/>"
		+ "Senha: %PASSWORD% <br/><br/>"
		+ "<a href=\"http://agitter.com\">Agitter</a><br /><br />"
		+ "Bons agitos,<br />Equipe Agitter.";

	public static void send(EmailAddress emailTo, String username, String password) throws IOException {

		PropertiesCredentials credentials = new PropertiesCredentials(
				ForgotPasswordMailDispatcher.class
						.getResourceAsStream("AwsCredentials.properties"));
		
		AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient(credentials);		

		Destination destination = new Destination(Arrays.asList(emailTo.toString()));
		Content subject = new Content(SUBJECT);
		String mailText = BODY.replaceAll("%USERNAME%", username).replaceAll("%PASSWORD%", password);
		Body body = new Body().withHtml(new Content(mailText));
		Message message = new Message(subject, body);
		service.sendEmail(new SendEmailRequest(from(), destination, message));
	}		
	
	private static String from() {
		return FROM_NAME + "<" + FROM + ">";
	}

}
