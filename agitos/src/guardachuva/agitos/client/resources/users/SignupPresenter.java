package guardachuva.agitos.client.resources.users;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.resources.BasePresenter;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SignupPresenter extends BasePresenter {

	public SignupPresenter(IController controller, RemoteApplicationAsync application) {
		super(controller, application);
	}

	public void trySignup(final String name, final String userName, final String password, final String email) {
		_application.createNewUser(name, userName, password, email, new AsyncCallback<SessionToken>() {
			
			@Override
			public void onSuccess(SessionToken result) {
				_controller.setSession(result);
				_controller.setLoggedUserEmail(email);
				_controller.redirectToRoot();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				notifyFailureListener(caught);
			}
		});
	}
}
