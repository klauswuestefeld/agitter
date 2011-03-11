package samples.jetty_prevayler3.simpleservlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


public class SimpleHandler extends AbstractHandler {

	private final SimpleServlet _handler;

	public SimpleHandler(SimpleServlet servlet) {
		_handler = servlet;
	}

	@Override
	public void handle(String targetIgnored, Request baseRequest,
			HttpServletRequest request, final HttpServletResponse response)
			throws IOException, ServletException {
		baseRequest.setHandled(true);

		_handler.service(new SimpleRequest(request.getRequestURI()),
				new SimpleResponse(response));
	}

}