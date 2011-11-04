package agitter.uiold.view;

import agitter.uiold.view.authentication.AuthenticationView;
import agitter.uiold.view.session.SessionView;


public interface AgitterView {

	void showWarningMessage(String message);
	
	AuthenticationView authenticationView();

	SessionView showSessionView();
	
}
