package guardachuva.agitos.server.prevalent;

import static sneer.foundation.environments.Environments.my;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;
import sneer.foundation.environments.Bindings;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Closure;

public class RPCCall implements Transaction {

	final String _args;

	public RPCCall(String arg0) {
		_args = arg0;
	}

	@Override
	public void executeOn(Object application, Date arg1) {
		final PrevalentRemoteApplicationService servlet = my(PrevalentRemoteApplicationService.class);

		Environment withAplicacao = EnvironmentUtils.compose(
				new Bindings(application).environment(), my(Environment.class));

		Environments.runWith(withAplicacao, new Closure() {
			@Override
			public void run() {
				serviceWith(servlet);
			}
		});
	}

	private void serviceWith(final PrevalentRemoteApplicationService servlet) {
		try {
			System.out.println(">>RPC: " + _args);
			final StringBuffer response = servlet.getResponse(_args);
			final String res = servlet.doProcessCall(_args);
			System.out.println("<<RPC: " + res);
			if(response!=null) {
				response.append(res);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			my(ExceptionLogger.class).log(ex);
		}
	}

	private static final long serialVersionUID = 1L;
}
