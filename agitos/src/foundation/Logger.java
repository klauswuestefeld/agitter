package foundation;

public class Logger {

	public static void log(Throwable t, String message) {
		System.err.println(message);
		t.printStackTrace();
	}

}
