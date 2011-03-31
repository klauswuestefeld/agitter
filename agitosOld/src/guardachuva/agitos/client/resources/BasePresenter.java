package guardachuva.agitos.client.resources;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

public abstract class BasePresenter {
	
	protected final IController _controller;
	protected final RemoteApplicationAsync _application;

	
	public BasePresenter(IController controller, RemoteApplicationAsync application) {
		_controller = controller;
		_application = application;
	}

	public String getLoggedUserEmail() {
		return _controller.getLoggedUserEmail();
	}

	protected void showError(Throwable caught) {
		_controller.showError(caught);
	}

	public boolean canDeleteEvent(EventDTO event) {
		return isLoggedUserEmail(event.getModerator().getEmail());
	}

	private boolean isLoggedUserEmail(String email) {
		return getLoggedUserEmail().equals(email);
	}

	public void logout() {
		_controller.logout();
	}

	protected SessionToken getSession() {
		return _controller.getSession();
	}
	
}