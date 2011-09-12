package infra.simploy;

import java.io.IOException;

public class Simploy {

	public static void run(String folder, int port, String password, final Trigger trigger) throws IOException {
		final Deployer deployer = new Deployer(folder, port);
		Reporter reporter = new Reporter(trigger, deployer);
		new SimployHttpServer(port + 1, password, trigger, reporter);
		new SimployMainLoop(deployer, trigger);
	}
	
}
