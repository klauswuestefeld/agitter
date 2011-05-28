package agitter.ui.view.impl;

import agitter.ui.view.SignupView;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class SignupViewImpl implements SignupView {

	private final ComponentContainer container;
	private final CssLayout signupView = new CssLayout();
	private final TextField email = new TextField("Email");
	private final TextField username = new TextField("Username");
	private final PasswordField password = new PasswordField("Senha");
	private final PasswordField passwordConfirmation = new PasswordField("Confirme a Senha");
	private final NativeButton signup = new NativeButton("Cadastrar!");
	private final NativeButton cancel = new NativeButton("Voltar");

	SignupViewImpl(ComponentContainer parent) {
		this.container = parent;
	}

	@Override
	public void show() {
		container.removeAllComponents();
		container.addComponent(signupView); signupView.addStyleName("a-signup-view");
		
			// Welcome messages
			Label label;
			label = new Label("Bem-vindo");
			signupView.addComponent(label); label.addStyleName("a-signup-welcome1");
			label = new Label("Complete seus dados e comece a agitar!");
			signupView.addComponent(label); label.addStyleName("a-signup-welcome2");
			// Signup Fields
			CssLayout signupfields = new CssLayout();
			signupView.addComponent(signupfields); signupfields.addStyleName("a-signup-fields");
				email.setDebugId("email");
				email.setSizeUndefined();
				signupfields.addComponent(email); email.addStyleName("a-signup-email");
				username.setDebugId("username");
				username.setSizeUndefined();
				signupfields.addComponent(username); username.addStyleName("a-signup-username");
				password.setDebugId("password");
				password.setSizeUndefined();
				signupfields.addComponent(password); password.addStyleName("a-signup-password");
				passwordConfirmation.setDebugId("passwordConfirmation");
				passwordConfirmation.setSizeUndefined();
				signupfields.addComponent(passwordConfirmation); passwordConfirmation.addStyleName("a-signup-password-confirmation");
				signupfields.addComponent(signup); signup.addStyleName("a-signup-button");
				signup.addStyleName("a-default-nativebutton");
				signupfields.addComponent(cancel); cancel.addStyleName("a-signup-cancel-button");
				cancel.addStyleName("a-default-nativebutton");
		
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

	@Override
	public void onSignupCancel(final Runnable runnable) {
		cancel.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

}
