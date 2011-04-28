package agitter.main;

import infra.processreplacer.ProcessReplacer;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class AgitterMain {

	private static final int HTTP_PORT = 8888;

	
	public static void main(String[] args) throws Exception {
		ProcessReplacer.start();
		
		initPrevalentSystem();
		runJetty();
	}


	private static void runJetty() throws Exception, InterruptedException {
		Server server = new Server(HTTP_PORT);
		
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.addHandler(resourcesContext()); 
		contexts.addHandler(vaadinContext());

		server.setHandler(contexts);
		server.start();
		
		System.out.println("Agitter started on PORT: " + HTTP_PORT);
		server.join();
	}

	
	private static WebAppContext resourcesContext() {
		WebAppContext result = new WebAppContext();
		result.setContextPath("/resources");
		result.setResourceBase("resources");
		result.setClassLoader(Thread.currentThread().getContextClassLoader());
		return result;
	}

	
	private static WebAppContext vaadinContext() {
		WebAppContext result = new WebAppContext();
		result.addServlet(vaadinServlet(), "/*");
		result.setResourceBase("ignored"); //Will cause an NPE if left null.
		result.setClassLoader(Thread.currentThread().getContextClassLoader());
		return result;
	}


	private static ServletHolder vaadinServlet() {
		ServletHolder result = new ServletHolder(com.vaadin.terminal.gwt.server.ApplicationServlet.class);
		result.setInitParameter("application", AgitterVaadinApplication.class.getName());
		return result;
	}

//Example WEB-INF/web.xml file to deploy Vaadin in Tomcat or other servlet server, if necessary:
//
//	<?xml version="1.0" encoding="UTF-8"?> <!--DO NOT USE DTD VALIDATION ON EXTERNAL SITE BECAUSE THAT SLOWS STARTUP CONSIDERABLY FOR TESTING-->
//	<web-app>
//	  <servlet>
//	    <servlet-name>agitter-servlet</servlet-name>
//	    <servlet-class>
//	        com.vaadin.terminal.gwt.server.ApplicationServlet
//	    </servlet-class>
//	    <init-param>
//	      <param-name>application</param-name>
//	      <param-value>agitter.main.AgitterVaadinApplication</param-value>
//	    </init-param>
//	  </servlet>
//	  <servlet-mapping>
//	    <servlet-name>agitter-servlet</servlet-name>
//	    <url-pattern>/*</url-pattern>
//	  </servlet-mapping>
//	</web-app>

	
	private static void initPrevalentSystem() {
		PrevaylerBootstrap.open(new File("prevalence"));
	}
	
}