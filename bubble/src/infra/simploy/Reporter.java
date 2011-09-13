package infra.simploy;

class Reporter {

	private final Trigger trigger;
	private final Deployer deployer;

	public Reporter(Trigger trigger, Deployer deployer) {
		this.trigger = trigger;
		this.deployer = deployer;
	}

	String report() {
		return
			deployer.status() + "\n" +
			trigger.status();
	}

}
