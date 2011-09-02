package infra.simploy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


class SimployHttpServer {

	private static final PrintStream SYSOUT = System.out;

	private static final int TCP_PORT = 44321;
	private static final int REQUEST_TIMEOUT = 1000 * 3;
	private static final String REPLY_HEADER =
		"HTTP/1.1 200 OK\r\n" +
		"Content-Type: text/plain\r\n" +
		"\r\n";

	private final String password;
	private final TriggerImpl deployTrigger;
	private final Reporter reporter;
	private final ServerSocket serverSocket;
	
	public SimployHttpServer(String password, TriggerImpl deployTrigger, Reporter reporter) throws IOException {
		this.password = password;
		this.deployTrigger = deployTrigger;
		this.reporter = reporter;
		this.serverSocket = new ServerSocket(TCP_PORT);
		
		SYSOUT.println("Listening for requests on port " + TCP_PORT);
		
		new Thread() { @Override public void run() {
			while (true)
				acceptRequest();
		}}.start();
	}

	
	private void acceptRequest() {
		try {
			tryToAcceptRequest();
		} catch (Exception e) {
			SYSOUT.println(e.getMessage());
		}
	}

	
	private void tryToAcceptRequest() throws Exception {
		Socket socket = serverSocket.accept();
		SYSOUT.println("Request Received");
		
		sendReport(socket);
		
		try {
			validateBuildRequest(socket);
		} finally {
			socket.close();
		}
		deployTrigger.urlHookReceived();
	}


	private void sendReport(Socket socket) throws Exception {
		OutputStream output = socket.getOutputStream();
		String reply = REPLY_HEADER + reporter.report(); 
		output.write(reply.getBytes("UTF-8"));
		output.flush();
	}

	
	private void validateBuildRequest(Socket socket) throws Exception {
		if (password == null) throwInvalid("Build requests are not being accepted.");
		
		socket.setSoTimeout(REQUEST_TIMEOUT);
		long t0 = System.currentTimeMillis();
		
		InputStream inputStream = socket.getInputStream();
		String request = "";
		while (!request.contains(password)) {
			int read = inputStream.read();
			if (read == -1) throwInvalid(request);
			request += (char)read;
			if (System.currentTimeMillis() - t0 > REQUEST_TIMEOUT) throwInvalid(request);
			if (request.length() > 2000) throwInvalid("<Request too large>");
		}
	}


	private void throwInvalid(String request) throws Exception {
		throw new Exception("Invalid Request: " + request);
	}
	
}


