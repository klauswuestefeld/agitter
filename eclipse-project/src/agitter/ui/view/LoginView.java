package agitter.ui.view;



public interface LoginView {

	void show();
	SignupView showSignupView(String email, String suggestedUserName, String password);

	String emailOrUsername();
	String password();
	
	void onLoginAttempt(Runnable loginAction, Runnable signupAction);
	void onForgotMyPassword(Runnable action);
	void onLogoClicked(Runnable action);

}
