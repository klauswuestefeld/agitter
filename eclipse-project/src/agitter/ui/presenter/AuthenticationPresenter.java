package agitter.ui.presenter;

import java.io.IOException;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.domain.users.Users.InvalidPassword;
import agitter.domain.users.Users.UserNotFound;
import agitter.email.ForgotPasswordMailDispatcher;
import agitter.ui.view.LoginView;
import agitter.ui.view.SignupView;

public class AuthenticationPresenter {

	private final Users users;
	private final LoginView loginView;
	private final Consumer<User> onAuthenticate;
	private final Consumer<String> warningDisplayer;
	private SignupView signupView;

	public AuthenticationPresenter(Users users, LoginView loginView, Consumer<User> onAuthenticate, Consumer<String> warningDisplayer) {
		this.users = users;
		this.loginView = loginView;
		this.onAuthenticate = onAuthenticate;
		this.warningDisplayer = warningDisplayer;

		this.loginView.onLoginAttempt(new Runnable() { @Override public void run() {
			loginAttempt();
		}});
		this.loginView.onForgotMyPassword(new Runnable() { @Override public void run() {
			forgotMyPassword();
		}});
		this.loginView.onLogoClicked(new Runnable() { @Override public void run() {
			logoClicked();
		}});
		
		this.loginView.show();
	}
	
	private void loginAttempt() {
		User user;
		Credential credential = new Credential(loginView.emailOrUsername(), loginView.password());
		try {
			user = credential.isEmailProvided()
				? users.loginWithEmail(credential.email(), credential.password())
				: users.loginWithUsername(credential.username(), credential.password());
			
		} catch (InvalidPassword e) {
			warningDisplayer.consume(e.getMessage());
			return;
		} catch (UserNotFound e) {
			showSignup(credential);
			return;
		}
		onAuthenticate.consume(user);
	}

	protected void logoClicked() {
		loginView.show();
	}

	private void forgotMyPassword() {
		if (loginView.emailOrUsername() == null || loginView.emailOrUsername().trim().length() == 0){
			warningDisplayer.consume("Preencha seu email ou username.");
			return;
		}
		User user;
		try{
			Credential credential = new Credential(loginView.emailOrUsername(), loginView.password());
			user = credential.isEmailProvided()
			? users.findByEmail(credential.email())
			: users.findByUsername(credential.username());
		} catch(UserNotFound e){
			warningDisplayer.consume(e.getMessage());
			return;
		}
		try {
			ForgotPasswordMailDispatcher.send(user.email(), user.username(), user.password());
			warningDisplayer.consume("E-mail enviado com sucesso!");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void showSignup(Credential credential) {
		signupView = loginView.showSignupView(credential);
		this.signupView.onSignupAttempt(new Runnable() { @Override public void run() {
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
			user = users.signup(signupView.username(), signupView.email(), signupView.password());
			onAuthenticate.consume(user);
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
		}
	}

	private boolean isPasswordConfirmed() {
		return signupView.password().equals(signupView.passwordConfirmation());
	}
	
}
