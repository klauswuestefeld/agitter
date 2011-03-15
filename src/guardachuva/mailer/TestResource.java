package guardachuva.mailer;

import guardachuva.agitos.shared.Mail;

import org.restlet.resource.Get;

public interface  TestResource {
	
    @Get
    public Mail retrieve();
 
}