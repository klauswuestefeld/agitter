package agitter.ui.view.authentication;


public interface SignupView {

	String password();
	String passwordConfirmation();

	void onSignupAttempt(Runnable action);

	void onCancel(Runnable action);

}
