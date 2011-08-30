package infra.simploy;


public class SimployMainLoop {

	public interface BuildDeployer {
		void deployLastGoodBuild();
		void deployNewBuild();
	}

	public interface DeploymentTrigger {
		void waitFor();
	}
	
	
	public SimployMainLoop(BuildDeployer deployer, DeploymentTrigger trigger) {
		deployer.deployLastGoodBuild();
		while (true) {
			trigger.waitFor();
			deployer.deployNewBuild();
		}
	}

}
