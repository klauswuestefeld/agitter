package agitter.ui.view;

import agitter.ui.view.authentication.AuthenticationView;
import agitter.ui.view.session.SessionView;


public interface AgitterView {

	void showWarningMessage(String message);
	
	AuthenticationView authenticationView();

	SessionView showSessionView();
	
}
