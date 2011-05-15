package agitter.ui.view.impl;

import agitter.ui.view.SignupView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SignupViewImpl implements SignupView {

	private final ComponentContainer container;
	private final VerticalLayout signupView = new VerticalLayout();
	private final TextField email = new TextField("Email");
	private final TextField username = new TextField("Username");
	private final PasswordField password = new PasswordField("Senha");
	private final PasswordField passwordConfirmation = new PasswordField("Confirme a Senha");
	private final Button signup = new Button("Cadastrar");

	SignupViewImpl(ComponentContainer container) {
		this.container = container;
	}

	@Override
	public void show(String email_,String username_, String password_) {
		container.removeAllComponents();
		container.addComponent(signupView); signupView.addStyleName("a-signup-view");
		signupView.setMargin(true);
		
		signupView.addComponent(new Label("Bem-vindo"));
		signupView.addComponent(new Label("Cadastre-se e comece a agitar!"));
		
		email.setValue(email_);
		signupView.addComponent(email);
		
		username.setValue(username_);
		signupView.addComponent(username);
		
		password.setValue(password_);
		signupView.addComponent(password);
		
		signupView.addComponent(passwordConfirmation);
		signupView.addComponent(signup);
		
		username.focus();
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
