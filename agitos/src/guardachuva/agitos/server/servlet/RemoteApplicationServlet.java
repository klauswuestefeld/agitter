package guardachuva.agitos.server.servlet;

import org.prevayler.gwt.rpcservlet.PrevalentRemoteServiceServlet;

public class RemoteApplicationServlet extends PrevalentRemoteServiceServlet {


	public RemoteApplicationServlet() throws Exception {
		super(createRemoteApplicationService());		
//		MailerServer.startRunning();
	}

	private static RemoteApplicationImpl createRemoteApplicationService() {
		return new RemoteApplicationImpl();
	}
	

	private static final long serialVersionUID = 1L;
}
