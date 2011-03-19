package guardachuva.agitos.server.prevalent;

import guardachuva.agitos.server.ApplicationImpl;
import guardachuva.agitos.server.mailer.MailerServer;

import javax.servlet.http.HttpServletRequest;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;

public class PrevalentRemoteServiceServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 1L;
	private static PrevalentRemoteServiceServlet _Instance;
	private Prevayler _prevayler;

	public PrevalentRemoteServiceServlet() throws Exception {
		this(createRemoteApplicationService());		
	}

	private static RemoteApplicationService createRemoteApplicationService() {
		return new RemoteApplicationService(new ApplicationImpl());
	}
	
	public PrevalentRemoteServiceServlet(RemoteApplicationService remoteApplication)
			throws Exception {
		super(remoteApplication);

		setThisAsInstance();

		final PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(remoteApplication);
		factory.configureTransactionFiltering(false);

		_prevayler = factory.create();
		MailerServer.startRunning();
	}

	private void setThisAsInstance() {
		_Instance = this;
	}

	@Override
	public String processCall(String args) throws SerializationException {
		final RPCCall call = new RPCCall(args);

		try {
			return (String)_prevayler.execute(call);
		} catch (RuntimeException e) {
			throw e;
		} catch (SerializationException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
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

	public static PrevalentRemoteServiceServlet GetInstance() {
		return _Instance;
	}
	
}
