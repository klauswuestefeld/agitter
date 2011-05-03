package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.Agitter;
import agitter.domain.User;
import agitter.ui.view.AuthenticationView;

public class AuthenticationPresenter {

	private final Agitter agitter;
	private final AuthenticationView view;
	private final Consumer<User> onAuthenticate;
	private final Consumer<String> errorDisplayer;

	public AuthenticationPresenter(Agitter agitter, AuthenticationView authenticationView, Consumer<User> onAuthenticate, Consumer<String> errorDisplayer) {
		this.agitter = agitter;
		this.view = authenticationView;
		this.onAuthenticate = onAuthenticate;
		this.errorDisplayer = errorDisplayer;

		this.view.show();
		this.view.onLoginAttempt(new Runnable() { @Override public void run() {
			loginAttempt();
		}});
	}

	private void loginAttempt() {
		User user;
		try {
			user = agitter.signup(view.fullName(), view.login(), view.email(), view.password());
			onAuthenticate.consume(user);
		} catch (Refusal e) {
			errorDisplayer.consume(e.getMessage());
		}
	}
	
}
