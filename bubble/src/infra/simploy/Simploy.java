package infra.simploy;

import java.io.File;

public class Simploy {

	public static void main(String[] args) {
		new BuildDeployerImpl(new File("builds")).deployNewBuild();
	}
	
}
