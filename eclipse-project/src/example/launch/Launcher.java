package example.launch;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Launcher {

	private static String contextPath = "/";
	private static String resourceBase = "web-files";
	private static int httpPort = 8888;

	public static void main(String[] args) throws Exception {
		Server server = new Server(httpPort);
		server.setHandler(webapp());
		server.start();
		
		System.out.println("Started Jetty " + Server.getVersion() + ", go to http://localhost:" + httpPort + contextPath);
		server.join();
	}

	
	private static WebAppContext webapp() {
		WebAppContext result = new WebAppContext();
		result.setContextPath(contextPath);
		result.setResourceBase(resourceBase);
		result.setClassLoader(Thread.currentThread().getContextClassLoader());
		return result;
	}

}