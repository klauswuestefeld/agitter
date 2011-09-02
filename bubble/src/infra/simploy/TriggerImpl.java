package infra.simploy;

class TriggerImpl implements SimployMainLoop.DeploymentTrigger {

	private static final int FIVE_MINUTES = 1000 * 60 * 5;
	
	private int _gitPullFailures = 0;

	
	@Override
	synchronized
	public void waitFor() {
		while (!pullNewVersion())
			waitFiveMinutes();
	}


	synchronized
	void urlHookReceived() {
		notify();
	}

	
	String gitPullStatus() {
		return _gitPullFailures < 3
			? ""
			: "git pull FAILED " + _gitPullFailures + " times in a row.\n";
	}

	
	private void waitFiveMinutes() {
		try {
			wait(FIVE_MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean pullNewVersion() {
		try {
			String stdOut = CommandRunner.exec("git pull");
			_gitPullFailures = 0;
			return !stdOut.contains("Already up-to-date.");
		} catch (Exception e) {
			_gitPullFailures++;
			return false;
		}
	}
	
}
