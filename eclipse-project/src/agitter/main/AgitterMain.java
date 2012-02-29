package agitter.main;

import static agitter.main.AgitterProcess.port;
import infra.processreplacer.ProcessReplacer;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AgitterMain {

	public static void main(String[] args) throws Exception {
		initLogging();
		new ProcessReplacer(new AgitterProcess(), port() - 1);
	}

	
	private static final int LOG_FILE_SIZE_LIMIT = 9000000;
	private static final int LOG_FILE_ROTATION_COUNT = 5;
	private static final Level INFO = Level.INFO;

	static private void initLogging() {
		prepareLogger();
		logExceptions();
	}

	
	private static void logExceptions() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {  @Override public void uncaughtException(Thread thread, Throwable throwable) {
			Logger logger = Logger.getLogger("");
			logger.log(Level.WARNING, "Uncaught Exception in thread " + thread, throwable);
		}});
	}

	
	private static void prepareLogger() {
		Logger logger = Logger.getLogger("");
		String logFilePath = "agitter.log";
		try {
			FileHandler fh = new FileHandler(logFilePath, LOG_FILE_SIZE_LIMIT, LOG_FILE_ROTATION_COUNT, false);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
		} catch(IOException ioExc) {
			Logger.getLogger("").log(Level.SEVERE, "Error creating the log file handler!", ioExc);
		}
		logger.setLevel(INFO);
		logger.info("Log initialized");
	}

}