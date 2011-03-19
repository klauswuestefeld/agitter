package samples.jetty_prevayler2;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import samples.jetty_servlet.HelloServlet;
 
public class PrevalentHandler extends ServletContextHandler
{
	
	public PrevalentHandler(int options) {
		super(options);
	}

	@Override
    public void doHandle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
    	super.doHandle(target, baseRequest, new SerializableRequest(request), response);
    }
 
    
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        
        ServletContextHandler context = new PrevalentHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
 
        context.addServlet(new ServletHolder(new HelloServlet()),"/*");
        context.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")),"/it/*");
        context.addServlet(new ServletHolder(new HelloServlet("Bonjour le Monde")),"/fr/*");
 
        server.start();
        server.join();
    }

}