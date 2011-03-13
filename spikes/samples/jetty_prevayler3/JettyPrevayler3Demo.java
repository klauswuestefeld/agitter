package samples.jetty_prevayler3;

import org.eclipse.jetty.server.Server;

import samples.jetty_prevayler3.simpleprevalent.PrevalentSimpleServlet;
import samples.jetty_prevayler3.simpleservlet.SimpleHandler;

public class JettyPrevayler3Demo {

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        
        server.setHandler(new SimpleHandler(new PrevalentSimpleServlet(new HelloSimpleServlet(), new Aplicacao())));
 
        server.start();
        server.join();
    }
}
