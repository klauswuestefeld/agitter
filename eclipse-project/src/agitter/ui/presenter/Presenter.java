package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import agitter.domain.Agitter;
import agitter.domain.User;
import agitter.ui.view.AgitterView;
import agitter.ui.view.SessionView;

public class Presenter {

	private final Agitter _agitter;
	private final AgitterView _view;


	public Presenter(Agitter agitter, AgitterView view) {
		_agitter = agitter;
		_view = view;
		
		openAuthentication();
	}


	private void openAuthentication() {
		new AuthenticationPresenter(_agitter, _view.authenticationView(), onAuthenticate(), errorDisplayer());
	}


	private Consumer<User> onAuthenticate() {
		return new Consumer<User>() { @Override public void consume(User user) {
			SessionView sessionView = _view.showSessionView();
			new SessionPresenter(_agitter.events(), user, sessionView, onLogout(), errorDisplayer());
		}};
	}

	
	private Runnable onLogout() {
		return new Runnable() { @Override public void run() {
			openAuthentication();
		}};
	}


	private Consumer<String> errorDisplayer() {
		return new Consumer<String>() { @Override public void consume(String message) {
			_view.showErrorMessage(message);
		}};
	}
	
	
}

