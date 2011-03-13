package samples.jetty_prevayler3.simpleservlet;

import java.io.IOException;


public interface SimpleServlet {

	public abstract void service(SimpleRequest simpleRequest,
			ISimpleResponse response) throws IOException;

}