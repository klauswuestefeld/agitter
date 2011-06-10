package agitter.main;

import static agitter.main.JettyRunner.createServletApp;
import static agitter.main.JettyRunner.createStaticFileSite;
import static agitter.main.JettyRunner.runWebApps;
import infra.logging.LogInfra;
import infra.processreplacer.ProcessReplacer.ReplaceableProcess;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.jetty.webapp.WebAppContext;
import org.prevayler.bubble.PrevalentBubble;

import agitter.mailing.AmazonEmailSender;
import agitter.mailing.PeriodicScheduleMailer;

public class AgitterProcess implements ReplaceableProcess {

	@Override
	public void prepareToTakeOver() throws IOException, ClassNotFoundException {
		PrevaylerBootstrap.open(new File("prevalence"));
	}
	
	
	@Override
	public void takeOver() {
		startMailing();
		try {
			runWebApps(vaadinThemes(), vaadin());
		} catch (Exception e) {
			log(e, "Unable to start Vaadin web app.");
		}
	}


	@Override
	public void prepareToRetire() throws IOException, ClassNotFoundException {
		PrevalentBubble.enterReadOnlyMode("O Agitter está em modo de somente-leitura. Ele estará liberado para alterações daqui a alguns minutos.");
		//PrevaylerBootstrap.consolidateSnapshot();
	}

	
	@Override
	public void cancelRetirement() {
		PrevalentBubble.leaveReadOnlyMode();
	}


	@Override
	public void retire() {
		System.exit(0);
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


	private void startMailing() {
		AmazonEmailSender sender;
		try {
			sender = new AmazonEmailSender();
		} catch (IOException e) {
			log(e, "Mailing start failed.");
			return;
		}
		PeriodicScheduleMailer.start(PrevaylerBootstrap.agitter(), sender);
	}


	private void log(Exception e, String message) {
		LogInfra.getLogger(this).log(Level.SEVERE, message, e);
	}

}