package agitter.client;


import agitter.shared.SessionToken;

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
	void redirectToSocialAuth(String providerName);
	void start();
	
}