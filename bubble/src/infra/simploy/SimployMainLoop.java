package infra.simploy;


public class SimployMainLoop {

	public interface BuildDeployer {
		void deployGoodBuild();
		void deployNewBuild();
	}

	public interface DeploymentTrigger {
		void waitFor();
	}
	
	
	public SimployMainLoop(BuildDeployer deployer, DeploymentTrigger trigger) {
		deployer.deployGoodBuild();
		while (true) {
			trigger.waitFor();
			deployer.deployNewBuild();
		}
	}

}
