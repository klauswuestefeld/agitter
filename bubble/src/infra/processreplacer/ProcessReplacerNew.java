package infra.processreplacer;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import sneer.foundation.lang.Closure;
import sneer.foundation.lang.ClosureX;

/** Kills previous instances of this process. It connects to the ProcessReplacerPort (system property). It then listens on that port to kill itself (System.exit(0)) if it receives a connection.*/
public class ProcessReplacerNew {

	
	
	public static void main(String[] args) throws IOException {
		ProcessReplacerNew.start();
		System.out.println("started");
		while (true) sleepOneSecond();
	}
	
	
	
	
	private static final int _port = Integer.parseInt(property("Port", "44111"));

	
	public interface ReplaceableProcess {
		void prepareToTakeOver();
		void takeOver();
		void prepareToRetire();
		void cancelRetirement();
		void retire();
	}


	public ProcessReplacerNew(ReplaceableProcess process) {
		this.process = process;
		
		try {
			tryToTakeOver();
		} catch (Exception e) {
			this.process.retire();
		}
	}


	private void tryToTakeOver() {
		preparePreviousProcessToRetireIfNecessary();
		this.process.prepareToTakeOver();
		retirePreviousProcessIfNecessary();
		this.process.takeOver();
	}

	
	private final ReplaceableProcess process;

	
	private void preparePreviousProcessToRetireIfNecessary() {
	}

	
	private void retirePreviousProcessIfNecessary() {
	}


	
	
	
	
	
	
	
	private static final int READY_TO_RETIRE = 77;
	private static final int PLEASE_RETIRE = 88;



	private static ServerSocket _serverSocket;
	private static int _attemptCount = 0;
	private static boolean _previousSessionKillAttempted;

	private static ClosureX<Exception> preparationToTakeOver;
	private static ClosureX<Exception> preparationToDie;
	private static Closure recovery;

	
	public static void start() throws IOException {
		killPreviousSessionIfNecessary();
		startAcceptingKillRequests();
	}


	private static void startAcceptingKillRequests() throws IOException {
		Thread t = new Thread() { @Override public void run() {
			while (true)
				acceptKillRequest();
		}};
		t.setDaemon(true);
		t.start();
	}


	private static void killPreviousSessionIfNecessary() throws IOException {
		while (true) {
			try {
				_serverSocket = new ServerSocket(_port);
				break;
			} catch (BindException be) {
				sendKillRequestIfNecessary();
				sleepOneSecond();
				if (_attemptCount++ == 10)
					fail(be.getMessage());
			}
		}
	}


	private static void fail(String reason) throws IOException {
		throw new IOException(className() + " unable to kill previous process. " + reason);
	}


	private static String property(String property, String defaultValue) {
		String key = className() + property;
		return System.getProperty(key, defaultValue);
	}


	private static String className() {
		return ProcessReplacerNew.class.getSimpleName();
	}


	private static void sleepOneSecond() {
		try {
			Thread.sleep( 1000 * 1 );
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	
	private static void acceptKillRequest() {
		try {
			tryToAcceptRequest();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
	private static void tryToAcceptRequest() throws IOException {
		Socket request = _serverSocket.accept();
		if (isFromSameMachine(request))
			try {
				tryToDie(request);
			} catch (Exception e) {
				recovery.run();
			}
		request.close();
	}


	private static boolean isFromSameMachine(Socket request) {
		return "127.0.0.1".equals(request.getLocalAddress().getHostAddress());
	}


	private static void tryToDie(Socket request) throws IOException {
		try {
			preparationToDie.run();
		} catch (Exception e) {
			throw new IOException("Preparation to die failed. ", e);
		}
		send(request, READY_TO_RETIRE);
		waitForMessage(request, PLEASE_RETIRE);
		die();
	}


	private static void send(Socket request, int message) throws IOException {
		request.getOutputStream().write(message);
		request.getOutputStream().flush();
	}


	private static void die() {
		try { _serverSocket.close(); } catch (IOException e) {}
		System.out.println(className() + " exiting.");
		System.exit(0);
	}

	
	private static void sendKillRequestIfNecessary() throws IOException {
		if (_previousSessionKillAttempted) return;
		_previousSessionKillAttempted = true;
		sendKillRequest();
	}


	private static void sendKillRequest() throws IOException {
		Socket request = new Socket("127.0.0.1", _port);
		waitForMessage(request, READY_TO_RETIRE);

		try {
			preparationToTakeOver.run();
		} catch (Exception e) {
			fail("Preparation to take over failed. " + e.getMessage());
		}
		
		send(request, PLEASE_RETIRE);
		request.close();
	}


	private static void waitForMessage(Socket request, int expected) throws IOException {
		int reply = request.getInputStream().read();
		if (reply != expected)
			fail("Reply expected:" + expected + " Actual:" + reply);
	}

}
