package agitter.ui.view;

public interface AuthenticationView {

	void show();

	String login();

	String fullName();

	String email();

	String password();

	void onLoginAttempt(Runnable runnable);

}
