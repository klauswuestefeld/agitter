package agitter.ui.view;

public interface AgitterView {

	void showWarningMessage(String message);
	
	SessionView showSessionView();

	AuthenticationView authenticationView();

}
