package samples.restlet;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/**
 * Posso ter várias Applications, por exemplo a AgitosApplication
 * Depois teremos a ChurrasApplication e outras, todas em um único server.
 * Para separar em diversos servers que se comunicam é simples
 */
public class MyServerApplication extends Application {
	
	public static void main(String[] args) throws Exception {
		Component component = new Component();
        component.getClients().add(Protocol.FILE);
        component.getServers().add(Protocol.HTTP, 8182);
        component.getDefaultHost().attach(new MyServerApplication());
        component.start();  
        
        // http://wiki.restlet.org/docs_2.0/13-restlet/28-restlet/78-restlet/55-restlet.html
	}
	
	@Override
    public Restlet createInboundRoot() {
		// Create a root router  
		Router router = new Router(getContext());  
				  
		// Attach the resources to the router  
		router.attach("/users/{user}", UserServerResource.class);  

		return router;
	}

}

