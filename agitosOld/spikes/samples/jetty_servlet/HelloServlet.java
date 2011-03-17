package samples.jetty_servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
	
	private String _greeting="Hello World";
    public HelloServlet(){}
    public HelloServlet(String greeting)
    {
        this._greeting=greeting;
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>"+_greeting+"</h1>");
        //response.getWriter().println("session=" + request.getSession(true).getId());
    }
}