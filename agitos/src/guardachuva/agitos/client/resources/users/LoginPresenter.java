package guardachuva.agitos.client.resources.users;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.resources.BasePresenter;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoginPresenter extends BasePresenter {

	private final class LoginCallback implements AsyncCallback<SessionToken> {
		private final String email;

		private LoginCallback(String email) {
			this.email = email;
		}

		@Override
		public void onSuccess(SessionToken result) {
			_controller.setSession(result);
			_controller.setLoggedUserEmail(email);
			_controller.start();
		}

		@Override
		public void onFailure(Throwable caught) {
			notifyFailureListener(caught);
		}
	}


	public LoginPresenter(IController controller, RemoteApplicationAsync application) {
		super(controller, application);
	}
	
	public void tryLogin(final String email, final String password) {
		 _application.authenticate(email, password, new LoginCallback(email));
	}
	
	public void trySignup(final String name, final String email, final String password) {
		_application.createNewUser(name, email, password, email, new LoginCallback(email));
	}
}
