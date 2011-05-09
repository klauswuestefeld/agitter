package agitter.ui.view.impl;

import agitter.ui.view.AuthenticationView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class AuthenticationViewImpl implements AuthenticationView {

	private final ComponentContainer container;
	private final TextField email = new TextField("Email");
	private final TextField username = new TextField("Username");
	private final PasswordField password = new PasswordField("Senha");
	private final PasswordField passwordConfirmation = new PasswordField("Confirme a Senha");
	private final Button signup = new Button("Cadastrar");

	AuthenticationViewImpl(ComponentContainer container) {
		this.container = container;
	}

	@Override
	public void show() {
		container.removeAllComponents();
		container.addComponent(email);
		container.addComponent(username);
		container.addComponent(password);
		container.addComponent(passwordConfirmation);
		container.addComponent(signup);
	}

	@Override
	public String username() {
		return (String)username.getValue();
	}

	@Override
	public String email() {
		return (String)email.getValue();
	}

	@Override
	public String password() {
		return (String)password.getValue(); 
	}

	@Override
	public String passwordConfirmation() {
		return (String)passwordConfirmation.getValue(); 
	}

	@Override
	public void onSignupAttempt(final Runnable runnable) {
		signup.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

}
