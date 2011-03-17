package guardachuva.agitos.client.resources.users;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.resources.BasePresenter;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoginPresenter extends BasePresenter {

	private LoginWrapper _view;
	
	public LoginPresenter(IController controller, RemoteApplicationAsync application) {
		super(controller, application);
	}
	
	public void wrap() {
		_view = new LoginWrapper(this);
	}
	
	public void tryLogin(final String email, final String password) {
		_application.authenticate(email, password, new AsyncCallback<SessionToken>() {
			@Override
			public void onSuccess(SessionToken result) {
				_controller.setSession(result);
				_controller.setLoggedUserEmail(email);
				_controller.redirect("/");
			}
			@Override
			public void onFailure(Throwable caught) {
				showError(caught);
				_view.resetForm();
			}
		});
	}
}
