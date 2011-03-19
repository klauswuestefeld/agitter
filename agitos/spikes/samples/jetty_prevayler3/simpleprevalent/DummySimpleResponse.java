package samples.jetty_prevayler3.simpleprevalent;

import java.io.PrintWriter;

import samples.jetty_prevayler3.simpleservlet.ISimpleResponse;

public class DummySimpleResponse implements ISimpleResponse {

	private static final ISimpleResponse SINGLETON = new DummySimpleResponse();

	@Override
	public void setContentType(String string) {
	}

	@Override
	public void setStatus(int status) {
	}

	@Override
	public PrintWriter getWriter() {
		return new PrintWriter(System.out, true);
	}

	public static ISimpleResponse singleton() {
		return SINGLETON;
	}

}
