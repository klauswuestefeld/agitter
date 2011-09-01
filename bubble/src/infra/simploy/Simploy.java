package infra.simploy;

import infra.simploy.SimployMainLoop.DeploymentTrigger;

import java.io.File;

public class Simploy {

	public static void main(String[] args) {
		new SimployMainLoop(new BuildDeployerImpl(new File("../builds")), new DeploymentTrigger() {
			
			@Override
			public void waitFor() {
				System.out.println("TRIGGER SAINDO");
				System.exit(0);
			}
		});
	}
	
}
