package agitter.ui.presenter;

import static agitter.common.Portal.Facebook;
import static agitter.common.Portal.Google;
import static agitter.common.Portal.Twitter;
import static agitter.common.Portal.WindowsLive;
import static agitter.common.Portal.Yahoo;
import static agitter.domain.emails.EmailAddress.email;
import infra.logging.LogInfra;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpSession;

import basis.lang.Consumer;
import basis.lang.Functor;
import basis.lang.exceptions.Refusal;

import utils.Encoders;
import agitter.common.Portal;
import agitter.controller.mailing.EmailSender;
import agitter.controller.mailing.ForgotPasswordMailSender;
import agitter.controller.mailing.SignupEmailController;
import agitter.controller.oauth.OAuth;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.ui.view.authentication.AuthenticationView;
import agitter.ui.view.authentication.LoginView;
import agitter.ui.view.authentication.SignupView;

public class AuthenticationPresenter {

	private static final Functor<String, byte[]> HMAC = Encoders.hmacForKey("QqCoisa7@(*");
	private static final Random RANDOM = new Random();

	private final Users users;
	private final AuthenticationView authenticationView;
	private final Consumer<User> onAuthenticate;
	private final Consumer<String> warningDisplayer;
	private final Consumer<String> javascriptExecutor;
	private final EmailSender emailSender;
	private final SignupEmailController signups;
	private final OAuth oAuth;
	private final HttpSession httpSession;
	private final String context;
	private final Consumer<String> urlRedirector;
	private SignupView signupView;
	private LoginView loginView;
	private boolean retryingAuthentication = false;
	
	public AuthenticationPresenter(Users users, AuthenticationView authenticationView, Consumer<User> onAuthenticate, SignupEmailController signups, EmailSender emailSender, OAuth oAuth, Consumer<String> warningDisplayer, Consumer<String> javascriptExecutor, HttpSession httpSession, String context, Consumer<String> urlRedirector) {
		this.users = users;
		this.authenticationView = authenticationView;
		this.onAuthenticate = onAuthenticate;
		this.signups = signups;
		this.emailSender = emailSender;
		this.oAuth = oAuth;
		this.warningDisplayer = warningDisplayer;
		this.javascriptExecutor = javascriptExecutor;
		this.httpSession = httpSession;
		this.context = context;
		this.urlRedirector = urlRedirector;
		this.authenticationView.onLogoClicked(new Runnable() { @Override public void run() {
			startAuthentication();
		}});
		this.authenticationView.onEnterAttempt( new Runnable() { @Override public void run() {
			enterAttempt(); 
		}});
		this.authenticationView.onGoogleSignin(new Runnable() { @Override public void run() {
			signinAttempt(Google); 
		}});
		this.authenticationView.onWindowsSignin(new Runnable() { @Override public void run() {
			signinAttempt(WindowsLive); 
		}});
		this.authenticationView.onYahooSignin(new Runnable() { @Override public void run() {
			signinAttempt(Yahoo); 
		}});
		this.authenticationView.onFacebookSignin(new Runnable() { @Override public void run() {
			signinAttempt(Facebook); 
		}});
		this.authenticationView.onTwitterSignin(new Runnable() { @Override public void run() {
			signinAttempt(Twitter); 
		}});
		
		startAuthentication();
	}
	
	private void enterAttempt() {
		String email = authenticationView.email();
		User user = null;
		try {
			user = users.searchByEmail(email(email));
			retryingAuthentication = false;
		} catch (Refusal e) {
			if(!retryingAuthentication) {
				javascriptExecutor.consume(authenticationView.getRetryAuthenticationJavascript());
				retryingAuthentication = true;
			} else {
				warningDisplayer.consume(e.getMessage());
				retryingAuthentication = false;
			}
			return;
		}
		if (user != null && user.hasSignedUp())		// TODO Codigo duplicado em SignupEmailController.checkDuplicatedSignup()
			startLogin();
		else
			startSignup();
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

	
	private void signinAttempt(Portal portal) {
		try{
			String url = oAuth.signinURL(context, httpSession, portal);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o " + portal);
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

		String newPassword = generatePassword();
		try {
			ForgotPasswordMailSender.send(emailSender, user.email(), newPassword);
		} catch (IOException e) {
			LogInfra.getLogger(this).severe("Erro enviando senha para usuario: " + user.email() + " - " + e.getMessage());
			throw new Refusal("Não foi possível enviar seu email. Tente novamente mais tarde.");
		}
		user.setPassword(newPassword);
	}

	
	private String generatePassword() {
		return Encoders.toHex(HMAC.evaluate(System.nanoTime()+""+RANDOM.nextLong())).substring(0, 10);
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


