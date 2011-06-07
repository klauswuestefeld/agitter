package agitter.main;

import infra.processreplacer.ProcessReplacer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AgitterMain {

	public static void main(String[] args) throws Exception {
		initLogger();
		new ProcessReplacer(new AgitterProcess());
	}

	
	private static final int LOG_FILE_SIZE_LIMIT = 9000000;
	private static final int LOG_FILE_ROTATION_COUNT = 5;
	private static final Level INFO = Level.INFO;

	static private void initLogger() {
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