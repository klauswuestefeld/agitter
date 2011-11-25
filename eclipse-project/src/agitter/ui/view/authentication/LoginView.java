package agitter.ui.view.authentication;


public interface LoginView {

	String password();

	void onLoginAttempt(Runnable action);
	void onForgotMyPassword(Runnable action);

	void onCancel(Runnable action);

}
