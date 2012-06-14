package infra.simploy;

import java.io.IOException;
import java.io.InputStream;

import basis.util.concurrent.Latch;


public class StdOutCapturer extends Thread {

	private final InputStream inputStream;
	private final Latch captured = new Latch();
	private volatile boolean finishing = false;
	private boolean lastChance = false;
	
	private String result = "";
	private Exception exception;

	
	public StdOutCapturer(Process process) {
		this.inputStream = process.getInputStream();
		setDaemon(true);
		start();
	}

	
	@Override
	public void run() {
		try {
			tryToCapture();
		} catch (Exception e) {
			exception = e;
		} finally {
			captured.open();
		}

	}

	private void tryToCapture() throws IOException, InterruptedException {
		while (true) {
			if (inputStream.available() != 0) {
				if (readByteWasLast()) return;
			} else {
				if (doneWaiting()) return;
			}
		}
	}


	private boolean doneWaiting() throws InterruptedException {
		if (lastChance) return true;
		Thread.sleep(100); //Avoid busy wait.
		if (finishing) lastChance = true;
		return false;
	}


	private boolean readByteWasLast() throws IOException {
		int read = inputStream.read();
		if (read == -1) return true;
		result += (char)read;
		System.out.print((char)read);
		return false;
	}

	
	public String finishCapturing() throws Exception {
		finishing = true;
		captured.waitTillOpen();
		if (exception != null)
			throw exception;
		return result;
	}
	
}
