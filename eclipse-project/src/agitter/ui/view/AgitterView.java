package agitter.ui.view;


public interface AgitterView {

	void showWarningMessage(String message);
	
	LoginView loginView();

	SessionView showSessionView();
	
}
