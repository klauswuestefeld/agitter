package guardachuva.agitos.server.prevalent;

import guardachuva.agitos.server.socialauth.SocialAuthServlet;

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

import com.google.gwt.user.client.rpc.RemoteService;

public class PrevalentRpcServer {

	private static PrevalentRemoteServiceServlet rpcApplicationService;

	public static void startRunning(RemoteService application) throws Exception {
		int serverPort = Integer.parseInt(
				System.getProperty("server.port","8888"));

		Server server = new Server();
		
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(serverPort);
		server.addConnector(connector);

		ServletContextHandler servletsContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletsContextHandler.setContextPath("/agitos");
        
        rpcApplicationService = new PrevalentRemoteServiceServlet(application);
        servletsContextHandler.addServlet(new ServletHolder(rpcApplicationService),"/rpc");

        
        SocialAuthServlet socialAuthServlet = new SocialAuthServlet();
		servletsContextHandler.addServlet(new ServletHolder(socialAuthServlet),"/social_auth");

        ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });

		resourceHandler.setResourceBase("./war");

		// FIXME: JEB SocialAuth: Sessões são nescessarias
		SessionIdManager idManager = new HashSessionIdManager();
		SessionManager sessionManager = new HashSessionManager();
		SessionHandler sessionHandler = new SessionHandler(sessionManager);
		sessionManager.setIdManager(idManager);
		sessionManager.setSessionHandler(sessionHandler);
		/**/

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { servletsContextHandler, /*sessionHandler, /**/ resourceHandler});
		server.setHandler(handlers);

		server.start();
	}

}
