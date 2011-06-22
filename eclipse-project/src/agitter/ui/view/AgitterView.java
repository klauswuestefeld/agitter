package agitter.ui.view;

import agitter.ui.view.authentication.LoginView;
import agitter.ui.view.session.SessionView;


public interface AgitterView {

	void showWarningMessage(String message);
	
	LoginView loginView();

	SessionView showSessionView();
	
}
