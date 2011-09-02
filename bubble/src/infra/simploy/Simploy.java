package infra.simploy;

import infra.simploy.SimployMainLoop.DeploymentTrigger;

import java.io.File;

public class Simploy {

	public static void main(String[] args) {
		new SimployMainLoop(new BuildDeployerImpl(new File("../builds")), new DeploymentTrigger() {
			
			private boolean firstTime = true;

			@Override
			public void waitFor() {
				if (firstTime) {
					firstTime = false;
					System.out.println("TRIGGER DISPARANDO");
					return;
				}
				System.out.println("TRIGGER SAINDO");
				System.exit(0);
			}
		});
	}
	
}
