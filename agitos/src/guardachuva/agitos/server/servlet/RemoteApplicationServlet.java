package guardachuva.agitos.server.servlet;

import org.prevayler.gwt.rpcservlet.PrevalentRemoteServiceServlet;

public class RemoteApplicationServlet extends PrevalentRemoteServiceServlet {


	public RemoteApplicationServlet() throws Exception {
		super(createRemoteApplicationService());		
//		MailerServer.startRunning();
		System.out.println("Started Agitos RemoteApplicationServlet");
	}

	private static RemoteApplicationImpl createRemoteApplicationService() {
		System.out.println("Starting Agitos RemoteApplicationServlet");
		return new RemoteApplicationImpl();
	}
	

	private static final long serialVersionUID = 1L;
}
