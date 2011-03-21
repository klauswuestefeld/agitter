package guardachuva.agitos.client;

import guardachuva.agitos.shared.SessionToken;

import com.google.gwt.json.client.JSONValue;

public interface IController {

	void setSession(SessionToken session);
	SessionToken getSession();

	void setLoggedUserEmail(String userName);
	void logout();
	String getLoggedUserEmail();
	
	void showByToken(String token);
	
	void showMessage(String message);
	void showError(JSONValue jsonValue);
	void showError(String string);
	void showError(Throwable e);
	void redirectToLoginPage();
	void redirectToRoot();
	boolean isAtLoginPage();
	boolean isAtSignupPage();
	boolean isAtRootPage();
	void redirectToSignupPage();
	
}
