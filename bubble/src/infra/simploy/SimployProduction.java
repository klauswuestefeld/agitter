package infra.simploy;

import java.io.IOException;

public class SimployProduction {

	public static void main(String[] args) throws IOException, InterruptedException {
		Simploy.run("production", 80, args[0], new DailyTrigger());
	}
	
}
