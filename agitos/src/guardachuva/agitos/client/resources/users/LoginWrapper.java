
package guardachuva.agitos.client.resources.users;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class LoginWrapper {
	
	private final LoginPresenter _presenter;
	private TextBox userNameField;
	private PasswordTextBox passwordField;
	private Button loginButton;
		
	public LoginWrapper(LoginPresenter loginPresenter) {
		_presenter = loginPresenter;
		userNameField = TextBox.wrap(Document.get().getElementById("username"));
		passwordField = PasswordTextBox.wrap(Document.get().getElementById("password"));
		loginButton = Button.wrap(Document.get().getElementById("login"));
		
		passwordField.addKeyPressHandler(new KeyPressHandler() { @Override public void onKeyPress(KeyPressEvent event) {
			if (event.getCharCode() == (char) 13) tryLogin();
		}});
		
		loginButton.addClickHandler(new ClickHandler() { @Override public void onClick(ClickEvent event) {
			tryLogin();
		}});
	}
	
	private String getUserName() {
		return userNameField.getText();
	}
	
	private String getPassword() {
		return passwordField.getText();
	}
	
	void tryLogin() {
		if (!loginButton.isEnabled()) return;
		
		_presenter.tryLogin(getUserName(), getPassword());
		loginButton.setEnabled(false);
	}
	
	public void resetForm() {
		userNameField.setFocus(true);
		loginButton.setEnabled(true);
	}
}
