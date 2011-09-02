package infra.simploy;

import java.io.File;
import java.io.IOException;

public class Simploy {

	public static void main(String[] args) throws IOException {
		TriggerImpl trigger = new TriggerImpl();
		MonitoredDeployer deployer = new MonitoredDeployer(
			new BuildDeployerImpl(new File("../builds")));
		Reporter reporter = new Reporter(trigger, deployer);

		String password = args[0];
		new SimployHttpServer(password, trigger, reporter);
		
		new SimployMainLoop(deployer, trigger);
	}
	
}
