import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;


public class Simploy extends RunListener implements Runnable {


	private static final String USAGE =
		"Usage: " +
		"\n  java Simploy compileCommand compiledTestsRootFolder deployCommand [hookSecretPhrase]" +
		"\n" +
		"\nExample:" +
		"\n  java Simploy \"ant build\" ./bin/tests \"ant deploy\" secret123" +
		"\n" +
		"\nMake sure you are in a git repository and JUnit is on the classpath.";


	private String _compileCommand;
	private File _testsFolder;
	private String _deployCommand;

	private boolean _someTestHasFailed = false;

	private String _secret;
	private ServerSocket _serverSocket;
	private static final int TCP_PORT = 44321;
	private static final int DEPLOY_REQUEST_TIMEOUT = 1000 * 7;

	
	private static final int DOT_CLASS = ".class".length();

	
	public static void main(String[] args) throws Exception {
		new Simploy(args);
	}

	
	private Simploy(String[] args) throws Exception {
		parseArgs(args);
		
		if (_secret != null)
			startListeningForBuildRequests();
		
		while (true) {
			deployNewVersionIfAvailable();
			waitAFewMinutes();
		}
	}
	
	
	private void startListeningForBuildRequests() throws IOException {
		_serverSocket = new ServerSocket(TCP_PORT);
		System.out.println("Listening for requests on port " + TCP_PORT);
		
		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}


	@Override
	public void run() {
		while (true)
			try {
				acceptRequest();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}


	private void acceptRequest() throws Exception {
		Socket socket = _serverSocket.accept();
		System.out.println("Request Received");
		
		try {
			validateRequest(socket);
		} finally {
			socket.close();
		}
		deployNewVersionIfAvailable();
	}

	
	private void validateRequest(Socket socket) throws Exception {
		socket.setSoTimeout(DEPLOY_REQUEST_TIMEOUT);
		long t0 = System.currentTimeMillis();
		InputStream inputStream = socket.getInputStream();
		String request = "";
		while (!request.contains(_secret)) {
			int read = inputStream.read();
			if (read == -1) throwInvalid(request);
			request += (char)read;
			if (System.currentTimeMillis() - t0 > DEPLOY_REQUEST_TIMEOUT) throwInvalid(request);
			if (request.length() > 4000) throwInvalid(request);
		}
	}


	private void throwInvalid(String request) throws Exception {
		throw new Exception("Invalid Request: " + request);
	}


	synchronized
	private void deployNewVersionIfAvailable() {
		try {
			tryToDeployNewVersionIfAvailable();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


	private void tryToDeployNewVersionIfAvailable() throws Exception {
			boolean isUpToDate = gitPull();
			if (isUpToDate)
				return;
			
			exec(_compileCommand);
			runTests();
			if (_someTestHasFailed)
				return;
			
			exec(_deployCommand);
	}


	private boolean gitPull() throws Exception {
		String stdOut = exec("git pull");
		return stdOut.contains("Already up-to-date.");
	}


	private void runTests() throws Exception {
		_someTestHasFailed = false;
		JUnitCore junit = new JUnitCore();
		junit.addListener(this);
		junit.run(findTestClasses());
		System.out.println("\n");
	}

	
	@Override
	public void testStarted(Description description) throws Exception {
		System.out.println(description);
	}
	@Override
	public void testAssumptionFailure(Failure failure) {
		fail();
	}
	@Override
	public void testFailure(Failure failure) {
		fail();
	}
	private void fail() {
		System.out.println("FAILED\n");
		_someTestHasFailed = true;
	}


	private String exec(String command) throws Exception {
		System.out.println("Executing: " + command);
		
		Process process = Runtime.getRuntime().exec(command);
		printOut(process.getErrorStream());
		String stdOut = printOut(process.getInputStream());
		
		if (process.waitFor() != 0)
			exitWith("Command failed: " + command);

		System.out.println("\n");
		return stdOut;
	}


	private String printOut(InputStream inputStream) throws Exception {
		String result = "";
		while (true) {
			int read = inputStream.read();
			if (read == -1) return result;
			result += (char)read;
			System.out.print((char)read);
		}
	}


	private void parseArgs(String[] args) {
		try {
			tryToParseArgs(args);
		} catch (RuntimeException r) {
			exitWith(USAGE);
		}
	}


	private void exitWith(String message) {
		System.out.println(message);
		System.exit(-1);
	}


	private void tryToParseArgs(String[] args) {
		_compileCommand = args[0];
		_testsFolder = new File(args[1]);
		_deployCommand = args[2];
		if (args.length == 4) _secret = args[3];
	}


	private Class<?>[] findTestClasses() throws Exception {
		List<Class<?>> result = convertToClasses(testFileNames());
		if (result.size() == 0) throw new Exception("No test classes found in: " + _testsFolder.getAbsolutePath());
		return result.toArray(new Class[0]);
	}

	
	private List<String> testFileNames() throws Exception {
		List<String> result = new ArrayList<String>();
		if (!_testsFolder.exists() || !_testsFolder.isDirectory()) throw new Exception("Tests root folder not found or not a folder: " + _testsFolder);
		accumulateTestClassFileNames(result, _testsFolder);
		return result;
	}

	
	private void accumulateTestClassFileNames(List<String> classFiles, File folder) {
		for (File candidate : folder.listFiles())
			if (candidate.isDirectory())
				accumulateTestClassFileNames(classFiles, candidate);
			else
				accumulateTestClassFileName(classFiles, candidate);
	}

	
	private void accumulateTestClassFileName(List<String> classFiles, File file) {
		String name = file.getName();
		if (name.endsWith("Test.class"))
			classFiles.add(file.getAbsolutePath());
	}

	
	private List<Class<?>> convertToClasses(final List<String> classFilePaths) {
		List<Class<?>> result = new ArrayList<Class<?>>();

		String rootPath = _testsFolder.getAbsolutePath();

		for (String filePath : classFilePaths) {
			String className = className(rootPath, filePath);
			Class<?> c = classForName(className);
			if (!Modifier.isAbstract(c.getModifiers()))
				result.add(c);
		}
		return result;
	}

	
	private Class<?> classForName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
	
	
	private String className(String classpathRoot, String classFilePath) {
		if (!classFilePath.startsWith(classpathRoot)) throw new IllegalStateException("Class file: " + classFilePath + " should be inside subfolder of: " + classpathRoot);
		int afterRoot = classpathRoot.length() + 1;
		int beforeDotClass = classFilePath.length() - DOT_CLASS;
		return classFilePath.substring(afterRoot, beforeDotClass).replace('/', '.').replace('\\', '.');
	}

	
	private void waitAFewMinutes() {
		try {
			Thread.sleep(1000 * 60 * 5);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

}


