package agitter.ui.view;


public interface LoginView {

	void show();
	SignupView signupView();

	String emailOrUsername();
	String password();
	
	void onLoginAttempt(Runnable action);
	void onForgotMyPassword(Runnable action);

}
