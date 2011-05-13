package spikes.email;

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

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;


public class AmazonSimpleEmailServiceSpike {

	private static final String TO = "klauswuestefeld@gmail.com";
	
	private static final String FROM = "no-reply@agitter.com";
	private static final String FROM_NAME = "Agitter";
	private static final String SUBJECT = "Bem-Vindo";
	private static final String BODY = "Seja bem vindo!<br /><br />"
		+ "Agora você já pode visualizar e criar seus próprios agitos acessando o link:<br />"
		+ "<a href=\"http://agitter.com\">Agitter</a><br /><br />"
		+ "Bons agitos,<br />Equipe Agitter.";

	public static void main(String[] args) throws IOException {

		PropertiesCredentials credentials = new PropertiesCredentials(
				AmazonSimpleEmailServiceSpike.class
						.getResourceAsStream("AwsCredentials.properties"));
		
		AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient(credentials);		

        verifyAddressIfNecessary(service, FROM);
		
		Destination destination = new Destination(Arrays.asList(TO));
		Content subject = new Content(SUBJECT);
		Body body = new Body().withHtml(new Content(BODY));
		Message message = new Message(subject, body);
		service.sendEmail(new SendEmailRequest(from(), destination, message));
		
		System.exit(0);

	}		
	
	private static String from() {
		return FROM_NAME + "<" + FROM + ">"; // "Joe Smith<joe@gmail.com>"
	}

	/**
     * SES requires that the sender and receiver of each message be
     * verified through the service. The verifyEmailAddress interface will
     * send the given address a verification message with a URL they can
     * click to verify that address.
     */
	static void verifyAddressIfNecessary(AmazonSimpleEmailService service, String address) {
		ListVerifiedEmailAddressesResult verifiedEmails = service.listVerifiedEmailAddresses();
        if (verifiedEmails.getVerifiedEmailAddresses().contains(address)) return;

        service.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(address));
        System.out.println("Please check the email address " + address + " to verify it.");
        System.exit(0);
	}

}
