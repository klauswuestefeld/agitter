package guardachuva.agitos.server.prevalent;

import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.shared.Application;

import javax.servlet.http.HttpServletRequest;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.foundation.environments.Bindings;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.ClosureX;
import sneer.foundation.lang.ProducerX;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;

public class PrevalentRemoteApplicationService extends RemoteApplicationService {

	private final Environment _servletEnvironment;
	private Prevayler _prevayler;

	public PrevalentRemoteApplicationService(Application application)
			throws Exception {
		super(application);

		final PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(application);
		factory.configureTransactionFiltering(false);

		_servletEnvironment = EnvironmentUtils.compose(
				new Bindings(this).environment(), my(Environment.class));

		Environments.runWith(_servletEnvironment, new ClosureX<Exception>() {
			@Override
			public void run() throws Exception {
				_prevayler = factory.create();
			}
		});
	}

	@Override
	public String processCall(String args) throws SerializationException {
		final RPCCall call = new RPCCall(args);

		return EnvironmentUtils.produceIn(_servletEnvironment, new ProducerX<String, SerializationException>() { @Override public String produce() throws SerializationException {
			try {
				return (String)_prevayler.execute(call);
			} catch (RuntimeException e) {
				throw e;
			} catch (SerializationException e) {
				throw e;
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}});
	}

	String superProcessCall(String args) throws SerializationException {
		return super.processCall(args);
	}

	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		return RPC.getDefaultSerializationPolicy();
	}

	
	@Override
	protected void checkPermutationStrongName() throws SecurityException {

	}

	private static final long serialVersionUID = 1L;
}
