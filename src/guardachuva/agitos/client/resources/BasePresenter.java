package guardachuva.agitos.client.resources;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

public abstract class BasePresenter {
	
	protected final IController _controller;
	protected final RemoteApplicationAsync _application;

	
	public BasePresenter(IController controller, RemoteApplicationAsync application) {
		_controller = controller;
		_application = application;
	}

	public String getEmailLogado() {
		return _controller.getUserMail();
	}

	protected void showError(Throwable caught) {
		_controller.showError(caught);
	}
	
}
