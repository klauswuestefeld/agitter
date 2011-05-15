package agitter.ui.view;

import agitter.ui.presenter.Credential;

public interface SignupView {

	void show(Credential credential);

	String username();
	String email();
	String password();
	String passwordConfirmation();

	void onSignupAttempt(Runnable action);

}
