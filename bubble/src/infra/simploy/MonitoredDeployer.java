package infra.simploy;

import infra.simploy.SimployMainLoop.BuildDeployer;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

class MonitoredDeployer implements BuildDeployer {

	private static final PrintStream SYSOUT = System.out;
	
	private final BuildDeployer delegate;
	private StringBuffer outputsBeingCaptured;

	private Date latestBuildDate;

	private String latestBuildStatus;

	private StringBuffer latestBuildOutputs;

	private Date latestSuccessDate;

	
	MonitoredDeployer(BuildDeployer delegate) {
		this.delegate = delegate;
	}
	
	
	@Override
	public void deployGoodBuild() {
		monitor(new Runnable(){  @Override public void run() {
			delegate.deployGoodBuild();
		}});
	}

	
	@Override
	public void deployNewBuild() {
		monitor(new Runnable(){  @Override public void run() {
			delegate.deployNewBuild();
		}});
	}

	private void monitor(Runnable deployment) {
		startCapturingOutputs();

		latestBuildDate = new Date();
		latestBuildStatus = "IN PROGRESS...";
		latestBuildOutputs = outputsBeingCaptured;

		try {
			deployment.run();
			latestSuccessDate = latestBuildDate;
			latestBuildStatus = "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			latestBuildStatus = "FAILED";
		} finally {
			stopCapturingOutputs();
		}
		
	}

	
	String status() {
		return
		"Last build status: " + latestBuildStatus +
		"\nLast build date  : " + latestBuildDate +
		"\nLast success date: " + latestSuccessDate +
		"\n" +
		"\nLast build outputs (stdOut and stdErr): " +
		"\n" + latestBuildOutputs +
		"\n\n\n" +
		"\n-----------------------------------------------------" +
		"\nRAM used: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "MB  (Max " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB)" +
		"\nReport produced by Simploy.";
	}


	private void startCapturingOutputs() {
		outputsBeingCaptured = new StringBuffer();
		PrintStream filter = new PrintStream(new FilterOutputStream(SYSOUT) {  @Override public void write(int b) throws IOException {
			super.write(b);
			outputsBeingCaptured.append((char)b);
		}});
		System.setOut(filter);
		System.setErr(filter);
	}


	private void stopCapturingOutputs() {
		System.setOut(SYSOUT);
		System.setErr(SYSOUT);
	}

}
