package samples.jetty_prevayler;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
 
public class HelloWorld extends AbstractHandler
{
	private static Servidor servidor;
	
    @Override
	public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(servidor.processa(target));
    }
 
    public static void main(String[] args) throws Exception
    {
    	servidor = new Servidor();
    	
        Server server = new Server(8080);
        server.setHandler(new HelloWorld());
 
        server.start();
        server.join();
    }
}