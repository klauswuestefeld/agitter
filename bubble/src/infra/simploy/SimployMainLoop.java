package infra.simploy;


public class SimployMainLoop {

	public SimployMainLoop(Deployer deployer, Trigger trigger) {
		deployer.deployGoodBuild();
		while (true) {
			trigger.waitFor();
			deployer.deployNewBuild();
		}
	}

}
