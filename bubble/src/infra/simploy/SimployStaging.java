package infra.simploy;

import java.io.IOException;

public class SimployStaging {

	public static void main(String[] args) throws IOException, InterruptedException {
		Simploy.run("staging", 8888, args[0], new FiveMinuteTrigger());
	}
	
}
