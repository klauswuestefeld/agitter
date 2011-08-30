package infra.simploy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


class CommandRunner {

	static String exec(String command) throws Exception {
		return execIn(command, null);
	}


	static String execIn(String command, File workingFolder) throws Exception {
		Process process = startProcess(command, workingFolder);
		String stdOut = capture(process.getInputStream());

		if (process.waitFor() != 0)
			throw new Exception("Command failed: " + command);

		System.out.println("\n");
		return stdOut;
	}


	private static Process startProcess(String command, File workingFolder) throws IOException {
		System.out.println("Executing: " + command);
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command.split(" "));
		builder.redirectErrorStream(true);
		builder.directory(workingFolder);
		return builder.start();
	}


	private static String capture(InputStream inputStream) throws Exception {
		String result = "";
		while (true) {
			int read = inputStream.read();
			if (read == -1) return result;
			result += (char)read;
			System.out.print((char)read);
		}
	}

}
