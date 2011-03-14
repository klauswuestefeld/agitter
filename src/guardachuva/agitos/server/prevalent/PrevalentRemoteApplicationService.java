package guardachuva.agitos.server.prevalent;

import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.server.rpc.RemoteApplicationService;
import guardachuva.agitos.shared.Application;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.foundation.environments.Bindings;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Closure;
import sneer.foundation.lang.ClosureX;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.SerializationPolicy;

public class PrevalentRemoteApplicationService extends RemoteApplicationService {


	private final Environment _servletEnvironment;
	private Prevayler _prevayler;
	private final HashMap<String, StringBuffer> _responses = new HashMap<String, StringBuffer>();


	public PrevalentRemoteApplicationService(Application application) throws Exception {
		super(application);
		final PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(application);
		factory.configureTransactionFiltering(false);

		_servletEnvironment = EnvironmentUtils.compose(new Bindings(this).environment(), my(Environment.class));
		
		Environments.runWith(_servletEnvironment, new ClosureX<Exception>() { @Override public void run() throws Exception {
			_prevayler = factory.create();
		}});		
	}

	@Override
	public String processCall(String arg0) throws SerializationException {
		final String args = arg0;
		final RPCCall call = new RPCCall(args);

		createResponse(args);
		
		Environments.runWith(_servletEnvironment, new Closure() { @Override public void run() {
				try {
					_prevayler.execute(call);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}});
		return delReponse(args).toString();
	}

	String doProcessCall(String args) throws SerializationException {
		return super.processCall(args);
	}
	
	
	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		return RPC.getDefaultSerializationPolicy();
	}

	private void createResponse(String args) {
		_responses.put(args, new StringBuffer());
	}

	private StringBuffer delReponse(String args) {
		return _responses.remove(args);
	}

	public StringBuffer getResponse(String args) {
		return _responses.get(args);
	}
	
	private static final long serialVersionUID = 1L;
}
