package agitter.main;

import static agitter.main.JettyRunner.*;
import infra.processreplacer.ProcessReplacer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.webapp.WebAppContext;

public class AgitterMain {

	public static void main(String[] args) throws Exception {
		ProcessReplacer.start();
		
		PrevaylerBootstrap.open(new File("prevalence"));
		runWebApps(resources(), vaadin());
	}

	
	private static WebAppContext resources() {
		return createStaticFileSite("resources", "/resources");
	}


	private static WebAppContext vaadin() {
		return createServletApp(
			com.vaadin.terminal.gwt.server.ApplicationServlet.class,
			initWith(AgitterVaadinApplication.class),
			"/*"
		);
	}


	private static Map<String, String> initWith(Class<AgitterVaadinApplication> vaadinApp) {
		Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("application", vaadinApp.getName());
		return initParams;
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

}