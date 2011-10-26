package infra.processreplacer;

import infra.logging.LogInfra;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;

public class ProcessReplacer {

	private static final int READY_TO_RETIRE = 77;
	private static final int PLEASE_RETIRE = 88;
	private static final int CANCEL_RETIREMENT = 99;
	
	
	public interface ReplaceableProcess {
		void forgetAboutRunning();
		void prepareToRun() throws Exception;
		void run();
		void prepareToRetire() throws Exception;
		void cancelRetirement();
		void retire();
	}


	/** Retires previous ReplaceableProcess instances. It connects to port and then listens on that port to retire process if it receives a connection.*/
	public ProcessReplacer(ReplaceableProcess process, int port) {
		this.process = process;
		this.port = port;
		
		try {
			tryToTakeOver();
		} catch (Exception e) {
			cancelPreviousProcessRetirementIfNecessary();
			log(e, "Process replacer unable to take over. This process will not be run.");
			this.process.forgetAboutRunning();
		}
	}

	private final ReplaceableProcess process;
	private final int port;

	private Socket previousProcess;
	private ServerSocket mutex;
	
	

	private void cancelPreviousProcessRetirementIfNecessary() {
		if (previousProcess == null) return;
		try {
			sendMessage(previousProcess, CANCEL_RETIREMENT);
		} catch (IOException e) {
			log(e, "Exception trying to cancel retirement of previous process.");
		}
	}


	private void log(Exception e, String message) {
		LogInfra.getLogger(this).log(Level.SEVERE, message, e);
	}


	private void tryToTakeOver() throws Exception {
		preparePreviousProcessToRetireIfNecessary();
		process.prepareToRun();
		retirePreviousProcessIfNecessary();
		startAcceptingRetirementRequests();
		process.run();
	}

	
	private void preparePreviousProcessToRetireIfNecessary() throws IOException {
		try {
			tryToAquireMutex();
		} catch (SocketException be) {
			if(! (be instanceof BindException))  LogInfra.getLogger(this).warning("This environment is not throwing the expected BindException. It's throwing: " + be.getClass() );
			previousProcess = new Socket("127.0.0.1", port);
			waitForMessage(previousProcess, READY_TO_RETIRE);
		}
	}


	private void retirePreviousProcessIfNecessary() throws IOException {
		if (mutex != null) return;

		sendMessage(previousProcess, PLEASE_RETIRE);
		int attemptCount = 0;
		while (mutex == null)
			try {
				tryToAquireMutex();
			} catch (SocketException be) {
				if(! (be instanceof BindException))  LogInfra.getLogger(this).warning("This environment is not throwing the expected BindException. It's throwing: " + be.getClass() );
				waitOneSecond();
				if (attemptCount++ > 20)
					fail("Unable to aquire server socket after asking previous process to retire (tried " + attemptCount + " times).");
			}
	}

	
	private void tryToAquireMutex() throws IOException {
		mutex = new ServerSocket(port);
	}
	
	
	private void waitOneSecond() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) { }
	}


	private void startAcceptingRetirementRequests() throws IOException {
		new Thread("ProcessReplacer ServerSocket.") { { setDaemon(true); } @Override public void run() {
			while (!mutex.isClosed())
				acceptRetirementRequest();
		}}.start();
	}
	
	
	private void acceptRetirementRequest() {
		Socket request = null;
		try {
			request = mutex.accept();
			tryToHandleRetirementRequest(request);
		} catch (Exception e) {
			if (mutex.isClosed()) return;
			log(e, "Exception handling process retirement request.");
		} finally {
			if (request != null)
				try { request.close(); } catch (IOException e) { LogInfra.getLogger(this).warning(e.getMessage()); }
		}
	}
	
	
	private void tryToHandleRetirementRequest(Socket request) throws IOException {
		checkFromSameMachine(request);
		try {
			tryToRetire(request);
		} catch (Exception e) {
			log(e, "Exception trying to retire. Cancelling retirement...");
			process.cancelRetirement();
		}
	}
	
	
	private void tryToRetire(Socket request) throws IOException {
		try {
			process.prepareToRetire();
		} catch (Exception e) {
			throw new IOException("Preparation to retire failed. ", e);
		}
		sendMessage(request, READY_TO_RETIRE);
		waitForMessage(request, PLEASE_RETIRE);
		retire();
	}
	
	
	private void retire() {
		close();
		LogInfra.getLogger(this).log(Level.INFO, "Retiring...");
		process.retire();
	}


	public void close() {
		try { if (mutex != null) mutex.close(); } catch (IOException e) { LogInfra.getLogger(this).warning(e.getMessage()); }
	}

	
	private static void checkFromSameMachine(Socket request) throws IOException {
		if (!isFromSameMachine(request))
			throw new IOException("Retirement request received from external machine.");
	}


	private static boolean isFromSameMachine(Socket request) {
		if (!"127.0.0.1".equals(request.getLocalAddress().getHostAddress())) return false;
		if (!request.getRemoteSocketAddress().toString().startsWith("/127.0.0.1:")) return false;
		return true;
	}
	
	
	private static void waitForMessage(Socket socket, int expected) throws IOException {
		int reply = socket.getInputStream().read();
		if (reply != expected)
			fail("Reply expected:" + expected + " Actual:" + reply);
	}


	private static void sendMessage(Socket socket, int message) throws IOException {
		socket.getOutputStream().write(message);
		socket.getOutputStream().flush();
	}


	private static void fail(String reason) throws IOException {
		throw new IOException(className() + " failed. " + reason);
	}
	
	
	private static String className() {
		return ProcessReplacer.class.getSimpleName();
	}

}
