package agitter.ui.view;


public interface SignupView {

	void show();

	String username();
	String email();
	String password();
	String passwordConfirmation();

	void onSignupAttempt(Runnable action);

	void onSignupCancel(Runnable action);

}
