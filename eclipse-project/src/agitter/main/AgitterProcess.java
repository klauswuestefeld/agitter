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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.jetty.webapp.WebAppContext;
import org.prevayler.bubble.PrevalentBubble;

import sneer.foundation.lang.Clock;
import agitter.controller.Controller;

import com.vaadin.Application;

public class AgitterProcess implements ReplaceableProcess {

	private static final String PREVALENCE_DIR = "prevalence";

	private final long startupTime = Clock.currentTimeMillis();
	

	@Override
	public void forgetAboutRunning() {
		try {
			BuildFolders.markAsFailed(workingFolder(), "Unable to retire previous process.");
		} catch (IOException e) {
			log(e, e.getMessage());
		}
		System.exit(0);
	}

	
	@Override
	public void prepareToRun() throws Exception {
		if (isSuccessfulBuild() || isDevelopmentMode())
			prepareToRunAsSuccessfulBuild();
		else
			prepareToRunAsCandidateBuild();
	}


	private boolean isSuccessfulBuild() throws IOException {
		return workingFolder().equals(lastSuccessfulBuild());
	}


	private File lastSuccessfulBuild() throws IOException {
		return BuildFolders.findLastSuccessfulBuildFolderIn(workingFolder().getParentFile());
	}


	private void prepareToRunAsSuccessfulBuild() throws IOException, ClassNotFoundException {
		PrevaylerBootstrap.open(new File(PREVALENCE_DIR));
	}


	private void prepareToRunAsCandidateBuild() throws Exception {
		try {
			bringSnapshotFromPreviousGoodBuildIfNecessary();
			prepareToRunAsSuccessfulBuild();
			PrevaylerBootstrap.agitter().migrateSchemaIfNecessary();
			BuildFolders.markAsSuccessful(workingFolder());
		} catch (Exception e) {
			BuildFolders.markAsFailed(workingFolder(), e);
			throw e;
		}
	}


	private void bringSnapshotFromPreviousGoodBuildIfNecessary() throws IOException {
		File previousBuild = lastSuccessfulBuild();
		if (previousBuild == null) return; //This is the first build.
		
		File from = lastSnapshotFrom(previousBuild);
		File to = new File(PREVALENCE_DIR, from.getName());

		assertSnapshotIsRecent(from);
		
		LogInfra.getLogger(this).info("SNAPSHOT - Moving from " + from + " to " + to);
		to.getParentFile().mkdirs();
		if (!from.renameTo(to))
			throw new IOException("Unable to rename " + from +  " to " + to);
	}


	private void assertSnapshotIsRecent(File snapshot) {
		long snapshotTime = snapshot.lastModified();
		if (snapshotTime < filesystemPrecision(startupTime))
			throw new IllegalStateException("Previous build did not generate new snapshot (it might not be running). Snapshot time (" + format(snapshotTime) + ") is older than this build startup time (" + format(startupTime) + "). Snapshot: " + snapshot);
	}


	private String format(long time) {
		return time + " -> " + new Date(time).toString();
	}


	private File lastSnapshotFrom(File previousBuild) {
		File previousPrevalence = new File(previousBuild, PREVALENCE_DIR);
		File[] snaps = previousPrevalence.listFiles(new FilenameFilter(){  @Override public boolean accept(File dir, String name) {
			return name.endsWith("snapshot");
		}});
		
		if (snaps.length == 0) throw new IllegalStateException("No snapshots found in " + previousBuild);
		
		Arrays.sort(snaps);
		return snaps[snaps.length - 1];
	}


	@Override
	public void run() {
		startController();
		try {
			runWebApps(port(), vaadinThemes(), vaadin());
		} catch (Exception e) {
			log(e, "Unable to start Vaadin web app.");
		}
	}


	private void startController() {
		@SuppressWarnings("unused")
		Controller started = Controller.CONTROLLER;
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


	private static Map<String, String> initWith(Class<? extends Application> vaadinApp) {
		Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("application", vaadinApp.getName());
		return initParams;
	}


	private void log(Exception e, String message) {
		LogInfra.getLogger(this).log(Level.SEVERE, message, e);
	}

	
	private static File workingFolder() throws IOException {
		return new File(".").getCanonicalFile();
	}

	
	private static long filesystemPrecision(long millis) {
		return (millis / 2000) * 2000;
	}

	private static boolean isDevelopmentMode() throws IOException {
		return !BuildFolders.isBuild(workingFolder());
	}
	
	static int port() throws IOException {
		String property = System.getProperty("http.port", "8888");
		return Integer.parseInt(property);
	}

}

