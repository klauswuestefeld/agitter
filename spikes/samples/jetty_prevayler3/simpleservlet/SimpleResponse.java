package samples.jetty_prevayler3.simpleservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;


public class SimpleResponse implements ISimpleResponse {
	
	private HttpServletResponse _response;

	public SimpleResponse(HttpServletResponse response) {
		_response = response;
	}

	@Override
	public void setContentType(String string) {
		_response.setContentType(string);
	}

	@Override
	public void setStatus(int status) {
		_response.setStatus(status);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return _response.getWriter();
	}
}