package agitter.ui.view;

public interface SignupView {

	void show(String email, String username, String password);

	String username();
	String email();
	String password();
	String passwordConfirmation();

	void onSignupAttempt(Runnable action);

}
