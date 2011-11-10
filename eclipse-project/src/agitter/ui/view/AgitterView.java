package agitter.ui.view;

import agitter.ui.view.authentication.AuthenticationView;
import agitter.ui.view.session.SessionView;


public interface AgitterView {

	AuthenticationView authenticationView();
	SessionView showSessionView();
	
	void showWarningMessage(String message);
	
	void hideToAvoidBlinkAndRedirect(String url);
	void show();
	void setURIFragment(String fragment);
	
}
