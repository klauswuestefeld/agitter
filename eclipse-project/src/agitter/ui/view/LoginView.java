package agitter.ui.view;

import agitter.ui.presenter.Credential;


public interface LoginView {

	void show();
	SignupView showSignupView(Credential credential);

	String emailOrUsername();
	String password();
	
	void onLoginAttempt(Runnable action);
	void onForgotMyPassword(Runnable action);

}
