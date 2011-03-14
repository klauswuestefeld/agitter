package guardachuva.agitos.client.resources.users;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.resources.BasePresenter;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SignupPresenter extends BasePresenter {

	@SuppressWarnings("unused")
	private SignupWrapper _view;

	public SignupPresenter(IController controller, RemoteApplicationAsync application) {
		super(controller, application);
	}

	public void wrap() {
		_view = new SignupWrapper(this);
	}

	public void trySignup(final String name, final String userName, final String password, final String email) {
		_application.createNewUser(name, userName, password, email, new AsyncCallback<SessionToken>() {
			
			public void onSuccess(SessionToken result) {
				_controller.setSession(result);
				_controller.setLoggedUser(userName, password);
				_controller.redirect("/");	
			}
			
			public void onFailure(Throwable caught) {
				showError(caught);
			}
		});
	}
}
