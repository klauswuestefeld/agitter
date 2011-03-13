package guardachuva.agitos.server;

import guardachuva.agitos.server.application.ApplicationImpl;
import guardachuva.agitos.shared.Application;

public class AgitosServer {

	public static void startRunning() throws Exception {
		Application application = new ApplicationImpl();
		PrevalentRpcServer.startRunning(application);
	}

}
