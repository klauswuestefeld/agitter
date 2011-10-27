package agitter.ui.view.authentication;



public interface AuthenticationView {

	void show();
	SignupView showSignupView();
	LoginView showLoginView();

	String email();
	
	void onEnterAttempt(Runnable loginAction);
	void onLogoClicked(Runnable action);

}
