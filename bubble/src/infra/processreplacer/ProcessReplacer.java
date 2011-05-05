package infra.processreplacer;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/** Kills previous instances of this process. It deletes the ProcessReplacer.mutex file and connects to the ProcessReplacerPort (system property). It then listens on that port to kill itself (System.exit(0)) if it receives a connection and the mutex file was deleted.*/
public class ProcessReplacer {

	private static final int _port = Integer.parseInt(property("Port", "44111"));

	private static ServerSocket _serverSocket;
	private static int _attemptCount = 0;
	private static boolean _previousSessionKillAttempted;

	private static final File _pleaseDieFile = new File(className() + ".request");

	
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


	private static void deletePleaseDieFile() throws IOException {
		if (_pleaseDieFile.exists() && !_pleaseDieFile.delete())
			throw new IOException("Unable to delete " + _pleaseDieFile);
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
					throw new IOException(className() + " unable to kill previous process.", be);
			}
		}
	}


	private static String property(String property, String defaultValue) {
		String key = className() + property;
		return System.getProperty(key, defaultValue);
	}


	private static String className() {
		return ProcessReplacer.class.getSimpleName();
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

	
	private static void tryToAcceptRequest() throws Exception {
		deletePleaseDieFile();

		Socket request = _serverSocket.accept();
		request.close();

		if (_pleaseDieFile.exists());
			die();
	}


	private static void die() {
		try { _serverSocket.close(); } catch (IOException e) {}
		System.out.println(className() + " exiting.");
		System.exit(0);
	}

	
	private static void sendKillRequestIfNecessary() throws IOException {
		if (_previousSessionKillAttempted) return;
		_previousSessionKillAttempted = true;
		killPreviousSession();
	}


	private static void killPreviousSession() throws IOException {
		_pleaseDieFile.createNewFile();
		Socket request = new Socket("127.0.0.1", _port);
		request.close();
	}

}
