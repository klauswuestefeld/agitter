package agitter.ui.view.impl;

import agitter.ui.view.SignupView;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
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
	private final NativeButton signup = new NativeButton("Cadastrar");

	SignupViewImpl(ComponentContainer parent) {
		this.container = parent;
	}

	@Override
	public void show() {
		container.removeAllComponents();
		container.addComponent(signupView); signupView.addStyleName("a-signup-view");
		
			// Welcome messages
			Label label;
			label = new Label("Bem-vindo"); label.addStyleName("a-signup-welcome1");
			signupView.addComponent(label);
			label = new Label("Complete seus dados e comece a agitar!"); label.addStyleName("a-signup-welcome2");
			signupView.addComponent(label);

			// Signup Fields
			VerticalLayout signupfields = new VerticalLayout(); signupfields.addStyleName("a-signup-fields");
			signupfields.setSpacing(true);
			signupView.addComponent(signupfields);

			email.setWidth("170px");
			signupfields.addComponent(email);
			username.setWidth("170px");
			signupfields.addComponent(username);
			password.setWidth("170px");
			signupfields.addComponent(password);
			passwordConfirmation.setWidth("170px");
			signupfields.addComponent(passwordConfirmation);
			signupfields.addComponent(signup);
		
		setupFocus();
		signup.setClickShortcut( KeyCode.ENTER );
	}

	
	private void setupFocus() {
		VaadinUtils.focusOrder(email, username, password, passwordConfirmation, signup);
		
		String emailValue = (String)email.getValue();
		if (emailValue != null && !emailValue.isEmpty())
			username.focus();
		else
			email.focus();
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
