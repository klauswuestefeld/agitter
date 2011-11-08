package agitter.ui.presenter;

import static agitter.domain.emails.EmailAddress.email;
import infra.logging.LogInfra;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpSession;

import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.controller.mailing.EmailSender;
import agitter.controller.mailing.ForgotPasswordMailSender;
import agitter.controller.mailing.SignupEmailController;
import agitter.domain.emails.AddressValidator;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.ui.view.authentication.AuthenticationView;
import agitter.ui.view.authentication.LoginView;
import agitter.ui.view.authentication.SignupView;

public class AuthenticationPresenter {

	private final Users users;
	private final AuthenticationView authenticationView;
	private final Consumer<User> onAuthenticate;
	private final Consumer<String> warningDisplayer;
	private final EmailSender emailSender;
	private final SignupEmailController signups;
	private SignupView signupView;
	private LoginView loginView;
	private final HttpSession httpSession;
	private final URL context;

	
	public AuthenticationPresenter(Users users, AuthenticationView authenticationView, Consumer<User> onAuthenticate, SignupEmailController signups, EmailSender emailSender, Consumer<String> warningDisplayer, HttpSession httpSession, URL context) {
		this.users = users;
		this.authenticationView = authenticationView;
		this.onAuthenticate = onAuthenticate;
		this.signups = signups;
		this.emailSender = emailSender;
		this.warningDisplayer = warningDisplayer;
		this.httpSession = httpSession;
		this.context = context;
		this.authenticationView.onLogoClicked(new Runnable() { @Override public void run() {
			startAuthentication();
		}});
		this.authenticationView.onEnterAttempt( new Runnable() { @Override public void run() {
			enterAttempt(); 
		}});
		this.authenticationView.onGoogleSignin(new Runnable() { @Override public void run() {
			googleSigninAttempt(); 
		}});
		
		startAuthentication();
	}
	
	
	private void enterAttempt() {
		String email = authenticationView.email();
		User user = null;
		try {
			AddressValidator.validateEmail(email);
			user = users.searchByEmail(email(email));
			if (user != null && user.hasSignedUp())		// TODO Codigo duplicado em SignupEmailController.checkDuplicatedSignup()
				startLogin();
			else
				startSignup();
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
		}
	}


	private void loginAttempt() {
		User user;
		try {
			user = users.loginWithEmail(email(authenticationView.email()), loginView.password());
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		onAuthenticate.consume(user);
	}

	
	private void googleSigninAttempt() {
		SocialAuthConfig config = SocialAuthConfig.getDefault();
		try {
			String providerId = "google";

			//load configuration. By default load the configuration from oauth_consumer.properties. 
			//You can also pass input stream, properties object or properties file name.
			config.load();

			//Create an instance of SocialAuthManager and set config
			SocialAuthManager manager = new SocialAuthManager();
			manager.setSocialAuthConfig(config);
			
			//URL of YOUR application which will be called after authentication
			String successUrl = context.toString() + "/oauth";
			
			// get Provider URL to which you should redirect for authentication.
			// id can have values "facebook", "twitter", "yahoo" etc. or the OpenID URL
			String url = manager.getAuthenticationUrl(providerId, successUrl);
			
			// Store in session
			httpSession.setAttribute("authManager", manager);
	
			authenticationView.redirectTo(url);

		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar rede social.");
			LogInfra.getLogger(this).severe("Erro de SocialAuth: " + e.getMessage());
		}
	}

	
	private void startAuthentication() {
		authenticationView.show();
	}


	private void startSignup() {
		signupView = authenticationView.showSignupView();
		signupView.onSignupAttempt(new Runnable() { @Override public void run() {
			signupAttempt();
		}});
		signupView.onCancel(new Runnable() { @Override public void run() {
			startAuthentication();
		}});
	}

	
	private void startLogin() {
		loginView = authenticationView.showLoginView();
		this.loginView.onLoginAttempt( new Runnable() { @Override public void run() { 
			loginAttempt();
		}});
		this.loginView.onForgotMyPassword(new Runnable() { @Override public void run() {
			forgotMyPassword();
		}});
		loginView.onCancel(new Runnable() { @Override public void run() {
			startAuthentication();
		}});
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
		User user = users.findByEmail(email(authenticationView.email()));
		if (!user.hasSignedUp()) throw new Refusal("Usuário não cadastrado.");
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
		signups.initiateSignup(email(authenticationView.email()), signupView.password());
		startAuthentication();
		warningDisplayer.consume("Um Email de confirmação foi enviado pra você. (Verifique também na sua caixa de SPAM)");
	}


	private boolean isPasswordConfirmed() {
		return signupView.password().equals(signupView.passwordConfirmation());
	}

	
	private boolean isBlank(final String value) {
		return value == null || value.trim().equals( "" );
	}

}


