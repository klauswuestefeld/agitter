
package guardachuva.agitos.client.resources.users;
import guardachuva.agitos.client.resources.BaseValidationWrapper;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class LoginWrapper extends BaseValidationWrapper {
	
	private final LoginPresenter _presenter;
	private TextBox emailField = new TextBox();
	private PasswordTextBox passwordField = new PasswordTextBox();
	private Button loginButton = new Button("Entrar");
	private TextBox fullNameField = new TextBox();
	private CheckBox checkNewUser;
		
	public LoginWrapper(LoginPresenter loginPresenter) {
		_presenter = loginPresenter;
		_presenter.setFailureListener(this);
		
		addItems();
		
		passwordField.addKeyPressHandler(new KeyPressHandler() { @Override public void onKeyPress(KeyPressEvent event) {
			if (event.getCharCode() == (char) 13) submit();
		}});
		
		loginButton.addClickHandler(new ClickHandler() { @Override public void onClick(ClickEvent event) {
			submit();
		}});
	}
	
	private void addItems() {
		
		FlowPanel loginFormPanel = loginFormPanel();
		loginFormPanel.add(checkNewUserPanel());
		loginFormPanel.add(fullNamePanel());
		loginFormPanel.add(emailPanel());
		loginFormPanel.add(passwordPanel());
		loginFormPanel.add(loginButton);
		
		getMainContainer().add(loginFormPanel);
	}
	
	
	private FlowPanel passwordPanel() {
		FlowPanel passwordPanel = new FlowPanel();
		passwordPanel.addStyleName("input");
		passwordPanel.add(new Label("Senha:"));
		passwordPanel.add(passwordField);
		return passwordPanel;
	}
	

	private FlowPanel emailPanel() {
		FlowPanel emailPanel = new FlowPanel();
		emailPanel.addStyleName("input");
		emailPanel.add(new Label("Email:"));
		emailPanel.add(emailField);
		return emailPanel;
	}
	

	private FlowPanel fullNamePanel() {
		final FlowPanel fullNamePanel = new FlowPanel();
		fullNamePanel.addStyleName("input");
		fullNamePanel.add(new Label("Nome:"));
		fullNamePanel.add(fullNameField);
		fullNamePanel.setVisible(isNewUser());
		
		checkNewUser.addClickHandler(new ClickHandler(){ @Override public void onClick(ClickEvent arg0) {
			fullNamePanel.setVisible(isNewUser());
		}});
		return fullNamePanel;
	}
	

	private FlowPanel checkNewUserPanel() {
		checkNewUser = new CheckBox("Sou Novo Usuário");
		FlowPanel newUserPanel = new FlowPanel();
		newUserPanel.add(checkNewUser);
		newUserPanel.add(new HTML("<br/>"));
		return newUserPanel;
	}
	

	private FlowPanel loginFormPanel() {
		FlowPanel loginForm = new FlowPanel();
		loginForm.addStyleName("forms");
		loginForm.addStyleName("login-form");
		loginForm.add(new HTML("<h1>Agite alguma coisa com seus amigos.</h1>"));
		return loginForm;
	}
	

	private String getFullName() {
		return fullNameField.getText();
	}
	

	private Boolean isNewUser() {
		return checkNewUser.getValue();
	}
	

	private String getEmail() {
		return emailField.getText();
	}
	
	
	private String getPassword() {
		return passwordField.getText();
	}
	
	
	private void submit() {
		if (!loginButton.isEnabled()) return;
		loginButton.setEnabled(false);
		
		if (isNewUser())
			_presenter.trySignup(getFullName(), getEmail(), getPassword());
		else
			_presenter.tryLogin(getEmail(), getPassword());
	}
	
	
	private void enableForm() {
		emailField.setFocus(true);
		loginButton.setEnabled(true);
	}

	
	@Override
	public void onFailure(String... errorMessages) {
		super.onFailure(errorMessages);
		enableForm();
	}

	
	@Override
	protected RootPanel getMainContainer() {
		return RootPanel.get(MAIN_CONTAINER_ID);
	}
	
}
