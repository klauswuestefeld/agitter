package agitter.ui.view;

public interface AuthenticationView {

	void show();

	String username();
	String email();
	String password();
	String passwordConfirmation();

	void onSignupAttempt(Runnable runnable);

}
