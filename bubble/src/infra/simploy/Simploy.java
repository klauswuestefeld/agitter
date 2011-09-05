package infra.simploy;

import java.io.File;
import java.io.IOException;

public class Simploy {

	public static void main(String[] args) throws IOException {
		TriggerImpl trigger = new TriggerImpl();
		MonitoredDeployer deployer = new MonitoredDeployer(
			new BuildDeployerImpl(folder("../producao")));
		Reporter reporter = new Reporter(trigger, deployer);

		String password = args[0];
		new SimployHttpServer(password, trigger, reporter);
		
		new SimployMainLoop(deployer, trigger);
	}

	
	private static File folder(String path) {
		File result = new File(path);
		if (!result.exists() && !result.mkdir()) throw new IllegalStateException("Unable to create folder " + result.getAbsolutePath());
		return result;
	}
	
}
