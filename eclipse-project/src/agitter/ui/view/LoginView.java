package agitter.ui.view;

public interface LoginView {

	void show();

	String emailOrUsername();
	String password();
	
	void onLoginAttempt(Runnable action);

}
