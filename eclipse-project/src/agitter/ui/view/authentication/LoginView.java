package agitter.ui.view.authentication;



public interface LoginView {

	void show();
	SignupView showSignupView();

	String emailOrUsername();
	String password();
	
	void onLoginAttempt(Runnable loginAction);
	void onForgotMyPassword(Runnable action);
	void onLogoClicked(Runnable action);
	void onStartSignup(Runnable action);

}
