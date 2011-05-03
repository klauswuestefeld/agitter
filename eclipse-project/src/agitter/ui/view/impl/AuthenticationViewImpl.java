package agitter.ui.view.impl;

import agitter.ui.view.AuthenticationView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TextField;

public class AuthenticationViewImpl implements AuthenticationView {

	private final ComponentContainer container;
	private final TextField fullName = new TextField("Nome:");
	private final TextField login = new TextField("Login:");
	private final TextField email = new TextField("Email:");
	private final TextField password = new TextField("Senha:");
	private final Button ok = new Button("OK");

	AuthenticationViewImpl(ComponentContainer container) {
		this.container = container;
	}

	@Override
	public void show() {
		container.removeAllComponents();
		container.addComponent(fullName);
		container.addComponent(login);
		container.addComponent(email);
		container.addComponent(password);
		container.addComponent(ok);
	}

	@Override
	public String fullName() {
		return (String)fullName.getValue();
	}

	@Override
	public String login() {
		return (String)login.getValue();
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
	public void onLoginAttempt(final Runnable runnable) {
		ok.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

}
