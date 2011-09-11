package infra.simploy;

abstract class Trigger {
		
		private int _gitPullFailures = 0;

		
		synchronized
		void waitFor() {
			while (!pullNewVersion())
				waitQuietly();
		}


		private void waitQuietly() {
			try {
				wait(millisToWait());
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}


		synchronized
		void deployRequestReceived() {
			notify();
		}
		

		String status() {
			return _gitPullFailures < 3
				? ""
				: "git pull FAILED " + _gitPullFailures + " times in a row.\n";
		}

		
		protected abstract long millisToWait();
		
		
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