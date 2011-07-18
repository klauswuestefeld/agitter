package agitter.main;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyRunner {

	public static void runWebApps(WebAppContext... webApps) throws Exception {
		Server server = new Server(firstAvailablePort(80, 8888));
		server.setHandler(collect(webApps));
		server.start();

		server.join();
	}


	private static ContextHandlerCollection collect(WebAppContext... webApps) {
		ContextHandlerCollection collection = new ContextHandlerCollection();
		for (WebAppContext app : webApps)
			collection.addHandler(app);
		return collection;
	}

	
	private static int firstAvailablePort(int... ports) throws IOException {
		for (int port : ports)
			if (isAvailable(port))
				return port;

		throw new IOException("No ports available: " + Arrays.asList(ports));
	}


	private static boolean isAvailable(int port) throws IOException {
		try {
			new ServerSocket(port).close();
			System.out.println("Port available: " + port);
			return true;
		} catch (SocketException e) {
			if(! (e instanceof BindException))  Logger.getLogger(JettyRunner.class.getPackage().getName()).warning("This environment is not throwing the expected BindException. It's throwing: "+e.getClass());
			System.out.println("\n\n===== PORT NOT AVAILABLE: " + port);
			return false;
		}
	}
	
	
	public static WebAppContext createStaticFileSite(String resourceBase, String contextPath) {
		WebAppContext result = createWebApp(resourceBase);
		result.setContextPath(contextPath);
		return result;
	}


	public static WebAppContext createServletApp(Class<? extends Servlet> servlet, Map<String, String> initParams, String contextPath) {
		ServletHolder holder = new ServletHolder(servlet);
		for (String param : initParams.keySet())
			holder.setInitParameter(param, initParams.get(param));

		WebAppContext result = createWebApp("ignored"); //Causes NPE if left null.
		result.addServlet(holder, contextPath);
		return result;
	}

	
	private static WebAppContext createWebApp(String resourceBase) {
		WebAppContext result = new WebAppContext();
		result.setResourceBase(resourceBase);
		result.setClassLoader(Thread.currentThread().getContextClassLoader());
		return result;
	}

}