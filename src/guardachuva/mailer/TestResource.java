package guardachuva.mailer;

import guardachuva.mailer.core.Mail;

import org.restlet.resource.Get;

public interface  TestResource {
	
    @Get
    public Mail retrieve();
 
}