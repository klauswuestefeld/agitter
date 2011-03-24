package guardachuva.agitos.client.resources.users;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.resources.BasePresenter;
import guardachuva.agitos.client.resources.FailureListener;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SignupPresenter extends BasePresenter {

	private FailureListener _listener;

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
				String[] errorMessages = caught.getMessage().split("\n");
				if (_listener != null)
					_listener.onFailure(errorMessages);
			}
		});
	}
	
	public void addFailureListener(FailureListener listener){
		if (_listener != null)
			throw new IllegalStateException();
		_listener = listener;
	}
}
