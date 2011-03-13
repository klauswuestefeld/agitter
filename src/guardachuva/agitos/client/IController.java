package guardachuva.agitos.client;

import guardachuva.agitos.shared.SessionToken;

import com.google.gwt.json.client.JSONValue;

public interface IController {

	void setSession(SessionToken session);
	SessionToken getSession();
	
	void setLoggedUser(String userName, String password);
	void logout();
	String getUserMail();
	
	void redirect(String path);
	void showByToken(String token);
	
	void showMessage(String message);
	void showError(JSONValue jsonValue);
	void showError(String string);
	
}
