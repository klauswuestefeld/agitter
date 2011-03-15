package guardachuva.agitos.server.prevalent;

import guardachuva.agitos.shared.Application;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class PrevalentRpcServer {

	private static RemoteApplicationService _applicationServlet;

	public static void startRunning(Application application) throws Exception {
		int serverPort = Integer.parseInt(
				System.getProperty("server.port","8888"));

		Server server = new Server();
		
		_applicationServlet = new PrevalentRemoteApplicationService(application);

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(serverPort);
		server.addConnector(connector);

		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/agitos");
        servletContextHandler.addServlet(new ServletHolder(_applicationServlet),"/rpc");

        ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });

		resourceHandler.setResourceBase("./war");

		// FIXME: JEB SocialAuth
		SessionIdManager idManager = new HashSessionIdManager();
		SessionManager sessionManager = new HashSessionManager();
		SessionHandler sessionHandler = new SessionHandler(sessionManager);
		sessionManager.setIdManager(idManager);
		sessionManager.setSessionHandler(sessionHandler);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { servletContextHandler, sessionHandler, resourceHandler});
		server.setHandler(handlers);

		server.start();
	}

}
