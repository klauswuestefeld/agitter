package guardachuva.agitos.server;

import guardachuva.agitos.server.application.Application;
import guardachuva.agitos.server.application.IApplication;
import httprevayler.PrevalentServer;

public class AgitosServer {

	public static void startRunning() throws Exception {
		IApplication application = new Application();
		PrevalentServer.startRunning(application);
	}

}
