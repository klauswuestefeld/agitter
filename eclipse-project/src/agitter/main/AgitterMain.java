package agitter.main;

import static agitter.main.JettyRunner.createServletApp;
import static agitter.main.JettyRunner.createStaticFileSite;
import static agitter.main.JettyRunner.runWebApps;
import infra.processreplacer.ProcessReplacer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.webapp.WebAppContext;

import agitter.PeriodicScheduleNotificationDaemon;
import agitter.email.AmazonEmailSender;

public class AgitterMain {

	public static void main(String[] args) throws Exception {
		ProcessReplacer.start();
		File prevalenceDir = new File("prevalence");
		if (Arrays.binarySearch(args, "keep-prevalence") < 0) {
			clean(prevalenceDir);
		}
		PrevaylerBootstrap.open(prevalenceDir);
		PeriodicScheduleNotificationDaemon.start(PrevaylerBootstrap.agitter(), new AmazonEmailSender());
		runWebApps(vaadinThemes(), vaadin());
	}

	
	private static void clean(File prevalenceDir) throws IOException {
		if (!prevalenceDir.exists())
			return;
		
		for(File file : prevalenceDir.listFiles())
			if (!file.delete())
				throw new IOException("Unable to delete " + file);
		
		System.out.println("prevalence directory deleted.");
	}


	private static WebAppContext vaadinThemes() {
		return createStaticFileSite("vaadin-theme", "/VAADIN/themes/agitter");
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