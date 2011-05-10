package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.ui.view.AuthenticationView;

public class AuthenticationPresenter {

	private final Users users;
	private final AuthenticationView view;
	private final Consumer<User> onAuthenticate;
	private final Consumer<String> warningDisplayer;

	public AuthenticationPresenter(Users users, AuthenticationView authenticationView, Consumer<User> onAuthenticate, Consumer<String> warningDisplayer) {
		this.users = users;
		this.view = authenticationView;
		this.onAuthenticate = onAuthenticate;
		this.warningDisplayer = warningDisplayer;

		this.view.show();
		this.view.onSignupAttempt(new Runnable() { @Override public void run() {
			signupAttempt();
		}});
	}

	private void signupAttempt() {
		if (!isPasswordConfirmed()) {
			warningDisplayer.consume("Senha e confirmação devem ser iguais.");
			return;
		}
		
		User user;
		try {
			user = users.signup(view.username(), view.email(), view.password());
			onAuthenticate.consume(user);
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
		}
	}

	private boolean isPasswordConfirmed() {
		return view.password().equals(view.passwordConfirmation());
	}
	
}
