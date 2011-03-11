package samples.jetty_prevayler3.simpleprevalent;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import samples.jetty_prevayler3.simpleservlet.ISimpleResponse;
import samples.jetty_prevayler3.simpleservlet.SimpleRequest;
import samples.jetty_prevayler3.simpleservlet.SimpleServlet;
import sneer.foundation.environments.Bindings;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Closure;
import sneer.foundation.lang.ClosureX;

public class PrevalentSimpleServlet implements SimpleServlet {

	private final Environment _servletEnvironment;
	private Prevayler _prevayler;

	public PrevalentSimpleServlet(SimpleServlet simpleServlet, Object application) throws Exception {
		final PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(application);
		factory.configureTransactionFiltering(false);

		_servletEnvironment = new Bindings(simpleServlet).environment();
		
		Environments.runWith(_servletEnvironment, new ClosureX<Exception>() { @Override public void run() throws Exception {
			_prevayler = factory.create();
		}});		
	}

	@Override
	public void service(final SimpleRequest request, final ISimpleResponse response) throws IOException {
		Environments.runWith(_servletEnvironment, new Closure() { @Override public void run() {
			_prevayler.execute(new SimpleCommand(request, response));
			//_prevayler.execute(new Consulta(request, response));
		}});

	}

}
