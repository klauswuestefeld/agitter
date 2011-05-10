package agitter.ui.view.impl;

import agitter.ui.view.LoginView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class LoginViewImpl implements LoginView {

	private final ComponentContainer container;
	private final TextField emailOrUsername = new TextField("Email ou Username");
	private final PasswordField password = new PasswordField("Senha");
	private final Button login = new Button("Agitar!");

	LoginViewImpl(ComponentContainer container) {
		this.container = container;
	}

	@Override
	public void show() {
		container.removeAllComponents();
		container.addComponent(emailOrUsername);
		container.addComponent(password);
		container.addComponent(login);
	}

	@Override
	public String emailOrUsername() {
		return (String)emailOrUsername.getValue();
	}

	@Override
	public String password() {
		return (String)password.getValue(); 
	}

	@Override
	public void onLoginAttempt(final Runnable runnable) {
		login.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

}
