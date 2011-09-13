package infra.simploy;

class FiveMinuteTrigger extends Trigger {

	private static final int FIVE_MINUTES = 1000 * 60 * 5;

	private int _gitPullFailures = 0;
	private String lastPull;

	
	@Override
	synchronized
	void waitFor() {
		while (!pullNewVersion())
			waitQuietly(FIVE_MINUTES);
	}


	@Override
	String status() {
		return failureStatus() +
			"Last Pull:" +
			"\n" + lastPull;
	}


	private String failureStatus() {
		return _gitPullFailures == 0
			? ""
			: "git pull FAILED " + _gitPullFailures + " times in a row.\n\n";
	}

	
	protected boolean pullNewVersion() {
		try {
			String stdOut = CommandRunner.exec("git pull");
			_gitPullFailures = 0;
			if (stdOut.contains("Already up-to-date.")) return false;
			lastPull = stdOut;
			return true;
		} catch (Exception e) {
			_gitPullFailures++;
			return false;
		}
	}

	
}
