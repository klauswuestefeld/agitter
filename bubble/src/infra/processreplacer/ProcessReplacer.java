package infra.processreplacer;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/** Kills previous instances of this process. */
public class ProcessReplacer {

	private static final int REQUEST_TIMEOUT = 1000 * 3;

	private static final String _secret = property("Secret", "defaultSecret");
	private static final int _port = Integer.parseInt(property("Port", "44321"));

	private static ServerSocket _serverSocket;
	private static int _attemptCount = 0;
	private static boolean _previousSessionKillAttempted;

	
	public static void start() throws IOException {
		acquireLockOnServerPort();

		new Thread() { @Override public void run() {
			while (true)
				acceptKillRequest();
		}}.start();
	}


	private static void acquireLockOnServerPort() throws IOException {
		while (true) {
			try {
				_serverSocket = new ServerSocket(_port);
				break;
			} catch (BindException be) {
				killPreviousSessionIfNecessary();
				sleepOneSecond();
				if (_attemptCount++ == 10)
					throw new IOException(className() + " unable to kill previous process.", be);
			}
		}
	}


	private static String property(String property, String defaultValue) {
		String key = className() + property;
		String result = System.getProperty(key);
		if (result == null) {
			System.out.println("Warning: " + key + " system property not set. Using default value.");
			return defaultValue;
		}
		return result;
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
		Socket socket = _serverSocket.accept();
		System.out.println(className() + " request received...");

		try {
			validateKillRequest(socket);
		} finally {
			socket.close();
		}
		die();
	}


	private static void die() {
		System.out.println(className() + " exiting.");
		System.exit(0);
	}

	
	private static void killPreviousSessionIfNecessary() throws IOException {
		if (_previousSessionKillAttempted) return;
		_previousSessionKillAttempted = true;
		final Socket s = new Socket("127.0.0.1", _port);
		s.getOutputStream().write(_secret.getBytes());
		s.close();
	}

	
	private static void validateKillRequest(Socket socket) throws Exception {
		socket.setSoTimeout(REQUEST_TIMEOUT);
		long t0 = System.currentTimeMillis();

		InputStream inputStream = socket.getInputStream();
		String request = "";
		while (!request.contains(_secret)) {
			int read = inputStream.read();
			if (read == -1)
				throwInvalid(request);
			request += (char) read;
			if (System.currentTimeMillis() - t0 > REQUEST_TIMEOUT)
				throwInvalid(request);
			if (request.length() > 100)
				throwInvalid("<Request too large>");
		}
	}
	
	
	private static void throwInvalid(String request) throws Exception {
		throw new Exception("Invalid Request: " + request);
	}

}
