package org.prevayler.gwt.rpcservlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.XStreamSerializer;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;

public class PrevalentRemoteServiceServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 1L;
	private static PrevalentRemoteServiceServlet _Instance;
	private final Prevayler _prevayler;

	public PrevalentRemoteServiceServlet(RemoteService remoteService) throws Exception {
		super(remoteService);

		initSingleton();

		_prevayler = initPrevayler(remoteService);
	}


	private void initSingleton() {
		if (_Instance != null) throw new IllegalStateException();
		_Instance = this;
	}


	private Prevayler initPrevayler(RemoteService remoteService)
			throws IOException, ClassNotFoundException {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(remoteService);
		factory.configureTransactionFiltering(false);
		factory.configureSnapshotSerializer(new XStreamSerializer("UTF-8"));
		
		final Prevayler prevayler = factory.create();

		// TODO: ver como fazer para que não fiquem rodando
		new Thread( new Runnable() { @Override public void run() {
			while(true) {
				try {
					Thread.sleep(10000);
					prevayler.takeSnapshot();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			}});
		
		return prevayler; 
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

	
	static PrevalentRemoteServiceServlet GetInstance() {
		return _Instance;
	}
	
}
