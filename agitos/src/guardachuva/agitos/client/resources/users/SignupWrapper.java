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

public class SignupWrapper extends BaseValidationWrapper implements EntryPoint {

	private final SignupPresenter _presenter;
	private TextBox nameField;
	private PasswordTextBox passwordField;
	private TextBox emailField;
	private Button signupButton;
	public SignupWrapper(SignupPresenter signupPresenter) {
		_presenter = signupPresenter;
		_presenter.setFailureListener(this);
		nameField = TextBox.wrap(Document.get().getElementById("name"));
		passwordField = PasswordTextBox.wrap(Document.get().getElementById("password"));
		emailField = TextBox.wrap(Document.get().getElementById("email"));
		signupButton = Button.wrap(Document.get().getElementById("signup"));
		signupButton.addClickHandler(new ClickHandler() { @Override public void onClick(ClickEvent event) {
			trySignup();
		}});
		passwordField.addKeyPressHandler(new KeyPressHandler() { @Override public void onKeyPress(KeyPressEvent event) {
			if (event.getCharCode() == (char) 13) trySignup();
		}});
	}
	
	protected void trySignup() {
		_presenter.trySignup(nameField.getText(), "userName", 
			passwordField.getText(), emailField.getText());
	}
	
	@Override
	public void onModuleLoad() {
	}

	@Override
	protected RootPanel getContainerForValidation() {
		return RootPanel.get(MAIN_CONTAINER_ID);
	}
}
