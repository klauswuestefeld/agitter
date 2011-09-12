package infra.simploy;

import java.io.IOException;

public class Simploy {

	public static void main(String[] args) throws IOException, InterruptedException {
		startSimploy("homologacao", 8888, args[0], new FiveMinuteTrigger());
		startSimploy("producao", 80, args[0], new DailyTrigger());
		Thread.sleep(7000);
	}


	private static void startSimploy(String folder, int port, String password, final Trigger trigger) throws IOException {
		final Deployer deployer = new Deployer(folder, port);
		Reporter reporter = new Reporter(trigger, deployer);
		new SimployHttpServer(port + 1, password, trigger, reporter);
		new Thread(){@Override public void run() {
			new SimployMainLoop(deployer, trigger);
		}}.start();
	}

}
