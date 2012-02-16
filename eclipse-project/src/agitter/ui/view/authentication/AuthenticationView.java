package agitter.ui.view.authentication;

public interface AuthenticationView {

	void show();
	SignupView showSignupView();
	LoginView showLoginView();

	String email();
	
	void onEnterAttempt(Runnable loginAction);
	void onLogoClicked(Runnable action);

	void onGoogleSignin(Runnable action);
	void onWindowsSignin(Runnable action);
	void onYahooSignin(Runnable action);
	void onFacebookSignin(Runnable action);
	void onTwitterSignin(Runnable action);
	
	String getRetryAuthenticationJavascript();

}
