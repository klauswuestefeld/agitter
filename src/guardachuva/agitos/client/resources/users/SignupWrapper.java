package guardachuva.agitos.client.resources.users;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class SignupWrapper {

	private final SignupPresenter _presenter;
	private TextBox nameField;
	private TextBox userNameField;
	private PasswordTextBox passwordField;
	private TextBox emailField;
	private Button signupButton;

	public SignupWrapper(SignupPresenter signupPresenter) {
		_presenter = signupPresenter;
		nameField = TextBox.wrap(Document.get().getElementById("name"));
		userNameField = TextBox.wrap(Document.get().getElementById("username"));
		passwordField = PasswordTextBox.wrap(Document.get().getElementById("password"));
		emailField = TextBox.wrap(Document.get().getElementById("email"));
		signupButton = Button.wrap(Document.get().getElementById("signup"));
		
		signupButton.addClickHandler(new ClickHandler() { @Override public void onClick(ClickEvent event) {
			trySignup();
		}});
	}
	
	public String getName() {
		return nameField.getText();
	}
	
	public String getUserName() {
		return userNameField.getText();
	}
	
	public String getPassword() {
		return passwordField.getText();
	}
	
	public String getEmail() {
		return emailField.getText();
	}

	protected void trySignup() {
		_presenter.trySignup();
	}
	
}
