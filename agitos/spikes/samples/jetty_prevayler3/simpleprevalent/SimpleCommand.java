package samples.jetty_prevayler3.simpleprevalent;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.prevayler.SureTransactionWithQuery;

import samples.jetty_prevayler3.simpleservlet.ISimpleResponse;
import samples.jetty_prevayler3.simpleservlet.SimpleRequest;
import samples.jetty_prevayler3.simpleservlet.SimpleServlet;
import sneer.foundation.environments.Bindings;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Closure;

final class SimpleCommand implements SureTransactionWithQuery {

	private static final long serialVersionUID = 1L;

	private final SimpleRequest _request;
	private static ISimpleResponse _response = DummySimpleResponse.singleton(); // Not serialize

	public SimpleCommand(SimpleRequest request, ISimpleResponse response) {
		_request = request;
		_response = response;
	}

	@Override
	public Object executeAndQuery(Object aplicacao, Date arg1) {
		final SimpleServlet servlet = my(SimpleServlet.class);

		Environments.runWith(new Bindings(aplicacao).environment(),
				new Closure() {
					@Override
					public void run() {
						serviceWith(servlet);
					}
				});

		return null;
	}

	private void serviceWith(final SimpleServlet servlet) {
		try {
			servlet.service(_request, _response);
		} catch (IOException e) {
			_response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
	}

}