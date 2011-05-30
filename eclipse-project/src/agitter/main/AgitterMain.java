package agitter.main;

import static agitter.main.JettyRunner.createServletApp;
import static agitter.main.JettyRunner.createStaticFileSite;
import static agitter.main.JettyRunner.runWebApps;
import infra.processreplacer.ProcessReplacer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.jetty.webapp.WebAppContext;

import agitter.mailing.AmazonEmailSender;
import agitter.mailing.PeriodicScheduleMailer;

public class AgitterMain {

	public static void main(String[] args) throws Exception {
		initLogger();
		ProcessReplacer.start();

		PrevaylerBootstrap.open(new File("prevalence"));

		PeriodicScheduleMailer.start(PrevaylerBootstrap.agitter(), new AmazonEmailSender());
		runWebApps(vaadinThemes(), vaadin());
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


	private static final int LOG_FILE_SIZE_LIMIT = 9000000;
	private static final int LOG_FILE_ROTATION_COUNT = 5;
	private static final Level INFO = Level.INFO;

	static private void initLogger() {
		Logger logger = Logger.getLogger("");
		String logFilePath = "agitter.log";
//		Logger.getLogger("").setLevel(Level.WARNING);
		try {
			FileHandler fh = new FileHandler(logFilePath, LOG_FILE_SIZE_LIMIT, LOG_FILE_ROTATION_COUNT, false);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
		} catch (IOException ioExc) {
			Logger.getLogger("").log(Level.SEVERE, "Error creating the log file handler!", ioExc);
		}
		logger.setLevel(INFO);
		logger.info("Log initialized");
	}

}