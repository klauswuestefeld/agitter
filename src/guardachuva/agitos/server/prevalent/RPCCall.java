package guardachuva.agitos.server.prevalent;

import static sneer.foundation.environments.Environments.my;

import java.util.Date;

import org.prevayler.TransactionWithQuery;

import sneer.foundation.environments.Bindings;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.foundation.lang.ProducerX;

import com.google.gwt.user.client.rpc.SerializationException;

public class RPCCall implements TransactionWithQuery {

	final String _args;

	
	public RPCCall(String arg0) {
		_args = arg0;
	}

	
	@Override
	public Object executeAndQuery(Object application, Date arg1) throws SerializationException {
		final PrevalentRemoteApplicationService servlet = my(PrevalentRemoteApplicationService.class);

		Environment withAplicacao = EnvironmentUtils.compose(
				new Bindings(application).environment(), my(Environment.class));

		return EnvironmentUtils.produceIn(withAplicacao, new ProducerX<String, SerializationException>() { @Override public String produce() throws SerializationException {
			return serviceWith(servlet);
		}});
	}

	private String serviceWith(PrevalentRemoteApplicationService servlet) throws SerializationException {
		System.out.println(">>RPC: " + _args);
		String result;
		try {
			result = servlet.superProcessCall(_args);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
		System.out.println("<<RPC: " + result);
		return result;
	}

	private static final long serialVersionUID = 1L;

}
