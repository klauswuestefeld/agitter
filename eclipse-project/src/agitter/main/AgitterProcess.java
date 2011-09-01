package agitter.main;

import static agitter.main.JettyRunner.createServletApp;
import static agitter.main.JettyRunner.createStaticFileSite;
import static agitter.main.JettyRunner.runWebApps;
import infra.logging.LogInfra;
import infra.processreplacer.ProcessReplacer.ReplaceableProcess;
import infra.simploy.BuildFolders;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.jetty.webapp.WebAppContext;
import org.prevayler.bubble.PrevalentBubble;

import sneer.foundation.lang.Clock;
import agitter.mailing.AmazonEmailSender;
import agitter.mailing.PeriodicScheduleMailer;

public class AgitterProcess implements ReplaceableProcess {

	private static final String PREVALENCE_DIR = "prevalence";

	private final long startupTime = Clock.currentTimeMillis();
	

	@Override
	public void prepareToRun() throws Exception {
		try {
			String previousGoodBuild = System.getProperty("previous-good-build");
			LogInfra.getLogger(this).info("Previous Good Build: " + previousGoodBuild);
			if (previousGoodBuild != null)
				bringSnapshotFrom(previousGoodBuild);
				
			PrevaylerBootstrap.open(new File(PREVALENCE_DIR));
			PrevaylerBootstrap.agitter().migrateSchemaIfNecessary();
			
			BuildFolders.markAsSuccessful(localFolder());
			
		} catch (Exception e) {
			LogInfra.getLogger(this).log(Level.SEVERE, "Preparing to run", e);
			BuildFolders.markAsFailed(localFolder(), e);
			throw e;
		}
	}


	private void bringSnapshotFrom(String previousGoodBuildPath) throws IOException {
		File from = lastSnapshotFrom(previousGoodBuildPath);
		File to = new File(PREVALENCE_DIR, from.getName());

		assertSnapshotIsRecent(from);
		
		to.getParentFile().mkdirs();
		if (!from.renameTo(to))
			throw new IOException("Unable to rename " + from +  " to " + to);
	}


	private void assertSnapshotIsRecent(File snapshot) {
		long snapshotTime = snapshot.lastModified();
		if (snapshotTime < startupTime)
			throw new IllegalStateException("Snapshot time (" + format(snapshotTime) + ") is older than Agitter startup time (" + format(startupTime) + "). Snapshot: " + snapshot);
	}


	private String format(long time) {
		return new Date(time).toString();
	}


	private File lastSnapshotFrom(String previousGoodBuildPath) {
		File lastPrevalence = new File(previousGoodBuildPath, PREVALENCE_DIR);
		File[] files = lastPrevalence.listFiles(new FilenameFilter(){  @Override public boolean accept(File dir, String name) {
			return name.endsWith("snapshot");
		}});
		
		if (files.length == 0) throw new IllegalStateException("No snapshots found in " + previousGoodBuildPath);
		
		Arrays.sort(files);
		return files[files.length - 1];
	}


	@Override
	public void run() {
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
		PrevaylerBootstrap.consolidateSnapshot();
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

	
	private File localFolder() {
		try {
			return new File(getClass().getResource(".").toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

}

