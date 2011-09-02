package infra.simploy;

class Reporter {

	private final TriggerImpl trigger;
	private final MonitoredDeployer deployer;

	public Reporter(TriggerImpl trigger, MonitoredDeployer deployer) {
		this.trigger = trigger;
		this.deployer = deployer;
	}

	String report() {
		return
			trigger.gitPullStatus() +
			deployer.status();
	}

}
