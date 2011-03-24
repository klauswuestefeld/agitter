
package guardachuva.agitos.client.resources.users;
import guardachuva.agitos.client.resources.BaseValidationWrapper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class LoginWrapper extends BaseValidationWrapper implements EntryPoint {
	
	private final LoginPresenter _presenter;
	private TextBox userNameField;
	private PasswordTextBox passwordField;
	private Button loginButton;
	
		
	public LoginWrapper(LoginPresenter loginPresenter) {
		_presenter = loginPresenter;
		_presenter.setFailureListener(this);
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
	
	private void enableForm() {
		userNameField.setFocus(true);
		loginButton.setEnabled(true);
	}

	@Override
	public void onModuleLoad() {
	}
	
	@Override
	public void onFailure(String... errorMessages) {
		super.onFailure(errorMessages);
		enableForm();
	}

	@Override
	protected RootPanel getContainerForValidation() {
		return RootPanel.get(MAIN_CONTAINER_ID);
	}
}
