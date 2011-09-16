package agitter.ui.presenter;

import static agitter.domain.emails.EmailAddress.email;
import infra.logging.LogInfra;

import java.io.IOException;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.controller.mailing.EmailSender;
import agitter.controller.mailing.ForgotPasswordMailSender;
import agitter.controller.mailing.SignupEmailController;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.ui.view.authentication.LoginView;
import agitter.ui.view.authentication.SignupView;

public class AuthenticationPresenter {

	private final Users users;
	private final LoginView loginView;
	private final Consumer<User> onAuthenticate;
	private final Consumer<String> warningDisplayer;
	private final EmailSender emailSender;
	private final SignupEmailController signups;
	private SignupView signupView;

	
	public AuthenticationPresenter(Users users, LoginView loginView, Consumer<User> onAuthenticate, SignupEmailController signups, EmailSender emailSender, Consumer<String> warningDisplayer) {
		this.users = users;
		this.loginView = loginView;
		this.onAuthenticate = onAuthenticate;
		this.signups = signups;
		this.emailSender = emailSender;
		this.warningDisplayer = warningDisplayer;

		this.loginView.onLoginAttempt(
			new Runnable() { @Override public void run() { loginAttempt(); }}
		);
		this.loginView.onForgotMyPassword(new Runnable() { @Override public void run() {
			forgotMyPassword();
		}});
		this.loginView.onLogoClicked(new Runnable() { @Override public void run() {
			startLogin();
		}});
		this.loginView.onStartSignup(new Runnable() { @Override public void run() {
			startSignup();
		}});
		
		startLogin();
	}
	
	
	private void loginAttempt() {
		User user;
		try {
			user = users.loginWithEmail(email(loginView.email()), loginView.password());
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		onAuthenticate.consume(user);
	}

	
	private void startSignup() {
		signupView = loginView.showSignupView();
		signupView.onSignupAttempt(new Runnable() { @Override public void run() {
			signupAttempt();
		}});
		signupView.onSignupCancel(new Runnable() { @Override public void run() {
			startLogin();
		}});
	}

	
	private void startLogin() {
		loginView.show();
	}

	
	private void forgotMyPassword() {
		try {
			tryToSendPassword();
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		warningDisplayer.consume("E-mail enviado com sucesso!");
	}

	
	private void tryToSendPassword() throws Refusal {
		User user = users.findByEmail(email(loginView.email()));
		try {
			ForgotPasswordMailSender.send(emailSender, user.email(), user.password());
		} catch (IOException e) {
			LogInfra.getLogger(this).severe("Erro enviando senha para usuario: " + user.email() + " - " + e.getMessage());
			throw new Refusal("Não foi possível enviar seu email. Tente novamente mais tarde.");
		}
	}

	
	private void signupAttempt() {
		try {
			trySignup();
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
		}
	}

	
	private void trySignup() throws Refusal {
		if (this.isBlank(signupView.password())) throw new Refusal("Preencha a senha.");
		if (!isPasswordConfirmed()) throw new Refusal("Senha e confirmação devem ser iguais.");
		signups.initiateSignup(email(signupView.email()), signupView.password());
		startLogin();
		warningDisplayer.consume("Um Email de confirmação foi enviado pra você. (Verifique também na sua caixa de SPAM)");
	}


	private boolean isPasswordConfirmed() {
		return signupView.password().equals(signupView.passwordConfirmation());
	}

	
	private boolean isBlank(final String value) {
		return value == null || value.trim().equals( "" );
	}

}


