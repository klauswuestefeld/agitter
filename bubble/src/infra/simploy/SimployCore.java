package infra.simploy;

import java.io.File;

public class SimployCore {

	public SimployCore(File tmpFolder, CommandRunner commandRunner) {
		commandRunner.exec(tmpFolder, "ant build");
	}

}
