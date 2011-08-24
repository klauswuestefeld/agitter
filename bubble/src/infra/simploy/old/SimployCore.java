package infra.simploy.old;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;


class SimployCore {

	static String deployCommand;

	static final PrintStream SYSOUT = System.out;
	private static StringBuffer _outputsBeingCaptured;

	private static Date _lastBuildDate;
	private static String _lastBuildStatus;
	private static StringBuffer _lastBuildOutputs;

	private static Date _lastSuccessDate;
	private static int _gitPullFailures;
	
	
	synchronized
	static	void deploy() {
		startCapturingOutputs();
		try {
			tryToDeploy();
		} finally {
			stopCapturingOutputs();
		}
	}


	private static void tryToDeploy() {
		if (!pullNewVersion())
			return;
		
		_lastBuildDate = new Date();
		_lastBuildStatus = "IN PROGRESS...";
		_lastBuildOutputs = _outputsBeingCaptured;

		try {
			compileTestDeploy();
			_lastSuccessDate = _lastBuildDate;
			_lastBuildStatus = "SUCCESS";
			
		} catch (Exception e) {
			e.printStackTrace();
			_lastBuildStatus = "FAILED";
		}
	}


	private static boolean pullNewVersion() {
		try {
			String stdOut = exec("git pull");
			_gitPullFailures = 0;
			return !stdOut.contains("Already up-to-date.");
		} catch (Exception e) {
			_gitPullFailures++;
			return false;
		}
	}


	private static void compileTestDeploy() throws Exception {
		exec("ant build");
//		SimployTestsRunner.runAllTestsIn(_testsFolder);
//		exec("ant deploy");
	}
	
	
	private static void startCapturingOutputs() {
		_outputsBeingCaptured = new StringBuffer();
		PrintStream filter = new PrintStream(new FilterOutputStream(SYSOUT) {  @Override public void write(int b) throws IOException {
			super.write(b);
			_outputsBeingCaptured.append((char)b);
		}});
		System.setOut(filter);
		System.setErr(filter);
	}


	private static void stopCapturingOutputs() {
		System.setOut(SYSOUT);
		System.setErr(SYSOUT);
	}

	
	private static String exec(String command) throws Exception {
		return SimployCommandRunner.exec(command);
	}


	static String report() {
		return
			gitPullStatus() +
			"Last build status: " + _lastBuildStatus +
			"\nLast build date  : " + _lastBuildDate +
			"\nLast success date: " + _lastSuccessDate +
			"\n" +
			"\nLast build outputs (stdOut and stdErr): " +
			"\n" + _lastBuildOutputs +
			"\n\n\n" +
			"\n-----------------------------------------------------" +
			"\nRAM used: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "MB  (Max " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB)" +
			"\nReport produced by Simploy.";
	}


	private static String gitPullStatus() {
		return _gitPullFailures < 3
			? ""
			: "git pull FAILED " + _gitPullFailures + " times in a row.\n";
	}
	
}
