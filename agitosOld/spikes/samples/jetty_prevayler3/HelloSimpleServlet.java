package samples.jetty_prevayler3;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import samples.jetty_prevayler3.simpleservlet.ISimpleResponse;
import samples.jetty_prevayler3.simpleservlet.SimpleRequest;
import samples.jetty_prevayler3.simpleservlet.SimpleServlet;


public class HelloSimpleServlet implements SimpleServlet
{
    private static final long serialVersionUID = 1L;
	
	private String _greeting="Hello World";
    public HelloSimpleServlet(){}
    public HelloSimpleServlet(String greeting)
    {
        this._greeting=greeting;
    }


	@Override
	public void service(SimpleRequest simpleRequest, ISimpleResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = response.getWriter();
		writer.println("<h1>"+_greeting+"</h1>");
	}
}