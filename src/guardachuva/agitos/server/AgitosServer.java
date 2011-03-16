package guardachuva.agitos.server;

import guardachuva.agitos.server.prevalent.PrevalentRpcServer;
import guardachuva.agitos.server.prevalent.RemoteApplicationService;
import guardachuva.agitos.shared.Application;

public class AgitosServer {

	public static void startRunning() throws Exception {
		Application application = new ApplicationImpl();
		PrevalentRpcServer.startRunning(
			new RemoteApplicationService(application)
			);
	}

}
