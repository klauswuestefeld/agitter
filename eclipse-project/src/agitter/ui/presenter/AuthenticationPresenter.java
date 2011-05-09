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
		this.view.onSignupAttempt(new Runnable() { @Override public void run() {
			loginAttempt();
		}});
	}

	private void loginAttempt() {
		if (!isPasswordConfirmed()) {
			errorDisplayer.consume("Senha e confirmação devem ser iguais.");
			return;
		}
		
		User user;
		try {
			user = agitter.signup(view.username(), view.email(), view.password());
			onAuthenticate.consume(user);
		} catch (Refusal e) {
			errorDisplayer.consume(e.getMessage());
		}
	}

	private boolean isPasswordConfirmed() {
		return view.password().equals(view.passwordConfirmation());
	}
	
}
