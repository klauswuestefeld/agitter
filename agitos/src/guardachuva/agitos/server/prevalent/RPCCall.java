package guardachuva.agitos.server.prevalent;


import java.util.Date;

import org.prevayler.TransactionWithQuery;

import com.google.gwt.user.client.rpc.SerializationException;

public class RPCCall implements TransactionWithQuery {

	final String _args;
	
	public RPCCall(String arg0) {
		_args = arg0;
	}

	
	@Override
	public Object executeAndQuery(Object application, Date arg1) throws SerializationException {
		System.out.println(">>RPC: " + _args);
		final String result;
		try {
			result = PrevalentRemoteServiceServlet.GetInstance().superProcessCall(_args);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
		System.out.println("<<RPC: " + result);
		return result;
	}

	private static final long serialVersionUID = 1L;

}
