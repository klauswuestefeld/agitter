package agitter.ui.view;

public interface AgitterView {

	void showErrorMessage(String message);
	
	SessionView showSessionView();

	AuthenticationView authenticationView();

}
