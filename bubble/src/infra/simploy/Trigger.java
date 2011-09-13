package infra.simploy;


abstract class Trigger {
		
	abstract void waitFor();
	abstract String status();

	synchronized
	void deployRequestReceived() {
		notify();
	}
	
	protected void waitQuietly(long millisToWait) {
		try {
			wait(millisToWait);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
	
}