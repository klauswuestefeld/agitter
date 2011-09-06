package agitter.ui.view.authentication;


public interface SignupView {

	void show();

	String email();
	String password();
	String passwordConfirmation();

	void onSignupAttempt(Runnable action);

	void onSignupCancel(Runnable action);

}
