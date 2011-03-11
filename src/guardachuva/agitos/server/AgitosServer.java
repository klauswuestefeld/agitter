package guardachuva.agitos.server;

import guardachuva.agitos.server.application.Application;
import httprevayler.PrevalentServer;

public class AgitosServer {

	public static void startRunning() throws Exception {
		Application application = new Application();
		PrevalentServer.startRunning(application);
	}

}
