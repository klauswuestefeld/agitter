package infra.simploy;

import java.io.File;

public interface CommandRunner {

	void exec(File workingFolder, String command);

}
