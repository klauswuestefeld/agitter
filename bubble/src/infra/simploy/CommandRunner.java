package infra.simploy;
import java.io.File;
import java.io.IOException;


class CommandRunner {

	static String exec(String command) throws Exception {
		return execIn(command, null);
	}


	static String execIn(String command, File workingFolder) throws Exception {
		Process process = startProcess(command, workingFolder);

		StdOutCapturer stdOut = new StdOutCapturer(process);

		if (process.waitFor() != 0)
			throw new Exception("Command failed: " + command);
		
		String result = stdOut.finishCapturing();
		System.out.println("\n");
		return result;
	}


	private static Process startProcess(String command, File workingFolder) throws IOException {
		System.out.println("\n\nExecuting: " + command);
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command.split(" "));
		builder.redirectErrorStream(true);
		builder.directory(workingFolder);
		return builder.start();
	}

}
