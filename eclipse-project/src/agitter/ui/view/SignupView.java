package agitter.ui.view;


public interface SignupView {

	void show(String email_, String suggestedUserName_, String password_);

	String username();
	String email();
	String password();
	String passwordConfirmation();

	void onSignupAttempt(Runnable action);

}
