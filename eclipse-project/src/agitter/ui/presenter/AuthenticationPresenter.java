package agitter.ui.presenter;

import static agitter.domain.emails.EmailAddress.mail;

import java.io.IOException;

import infra.logging.LogInfra;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.mailing.ForgotPasswordMailDispatcher;
import agitter.ui.view.authentication.LoginView;
import agitter.ui.view.authentication.SignupView;

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
			user = users.loginWithEmail(mail(loginView.email()), loginView.password());
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
		if (loginView.email() == null || loginView.email().trim().length() == 0){
			warningDisplayer.consume("Preencha seu email.");
			return;
		}
		User user;
		try{
			user = users.findByEmail(mail(loginView.email()));
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		try {
			ForgotPasswordMailDispatcher.send(user.email(), user.password());
		} catch (IOException e) {
			warningDisplayer.consume("Não foi possível enviar seu email, por favor entre em contato com a equipe do Agitter.");
			LogInfra.getLogger(this).severe("Erro enviando senha para usuario: " + user.email() + " - " + e.getMessage());
			return;
		}
		warningDisplayer.consume("E-mail enviado com sucesso!");
	}

	private boolean isBlank(final String value) {
		return value == null || value.trim().equals( "" );
	}
	
	private void signupAttempt() {
		if( this.isBlank( signupView.email() ) ) {
			warningDisplayer.consume("Campo email deve ser especificado");
			return;			
		}
		
		if( this.isBlank( signupView.password() ) ) {
			warningDisplayer.consume("Campo senha deve ser especificado");
			return;			
		}

		if (!isPasswordConfirmed()) {
			warningDisplayer.consume("Senha e confirmação devem ser iguais.");
			return;
		}
		
		User user;
		try {
			user = users.signup(mail(signupView.email()), signupView.password());
			onAuthenticate.consume(user);
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
		}
	}

	private boolean isPasswordConfirmed() {
		return signupView.password().equals(signupView.passwordConfirmation());
	}
	
}
