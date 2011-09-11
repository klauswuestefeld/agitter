package agitter.main;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyRunner {

	public static void runWebApps(WebAppContext... webApps) throws Exception {
		Server server = new Server(port());
		server.setHandler(collect(webApps));
		server.start();

		server.join();
	}


	private static int port() throws IOException {
		String property = System.getProperty("agitter.port", "8888");
		return Integer.parseInt(property);
	}


	private static ContextHandlerCollection collect(WebAppContext... webApps) {
		ContextHandlerCollection collection = new ContextHandlerCollection();
		for (WebAppContext app : webApps)
			collection.addHandler(app);
		return collection;
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