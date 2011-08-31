package infra.simploy;

import java.io.IOException;
import java.io.InputStream;

import sneer.foundation.util.concurrent.Latch;

public class StdOutCapturer extends Thread {

	private final InputStream inputStream;
	private final Latch finished;

	private String result;
	private Exception exception;

	
	public StdOutCapturer(Process process) {
		this.inputStream = process.getInputStream();
		this.finished = new Latch();
		setDaemon(true);
		start();
	}

	
	@Override
	public void run() {
		result  = "";
		
		try {
			tryToCapture();
		} catch (Exception e) {
			exception = e;
		}

	}

	private void tryToCapture() throws IOException, InterruptedException {
		while (true) {
			while (inputStream.available() == 0) {
				if (finished.isOpen()) return;
				Thread.sleep(20); //Avoid busy wait.
			}
			int read = inputStream.read();
			if (read == -1) return;
			result += (char)read;
			System.out.print((char)read);
		}
	}

	
	public String result() throws Exception {
		finished.open();
		if (exception != null)
			throw exception;
		return result;
	}
	
}
