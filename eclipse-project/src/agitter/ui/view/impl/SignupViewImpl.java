package agitter.ui.view.impl;

import agitter.domain.users.Credential;
import agitter.ui.view.SignupView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class SignupViewImpl implements SignupView {

	private final ComponentContainer container;
	private final TextField email = new TextField("Email");
	private final TextField username = new TextField("Username");
	private final PasswordField password = new PasswordField("Senha");
	private final PasswordField passwordConfirmation = new PasswordField("Confirme a Senha");
	private final Button signup = new Button("Cadastrar");

	SignupViewImpl(ComponentContainer container) {
		this.container = container;
	}

	@Override
	public void show(Credential credential) {
		container.removeAllComponents();
		container.addComponent(new Label("Bem-vindo"));
		container.addComponent(new Label("Cadastre-se e comece a agitar!"));
		
		email.setValue(credential.getEmail());
		container.addComponent(email);
		
		username.setValue(credential.getUserName());
		container.addComponent(username);
		
		password.setValue(credential.getPassword());
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
