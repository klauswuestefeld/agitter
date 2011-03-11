package samples.restlet;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Cada ServerResource � como se fosse uma controller...
 */
public class UserServerResource extends ServerResource implements UserResource {
  
    @Get  
    public User getUser() {  
        return new User("Altieres", 28);  
    }
}