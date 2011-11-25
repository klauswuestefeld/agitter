package agitter.ui.view.authentication;

import vaadinutils.WidgetUtils;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;

public class SignupViewImpl implements SignupView {

	private final ComponentContainer container;
	private final CssLayout signupView = new CssLayout();
	private final PasswordField password = new PasswordField("Senha");
	private final PasswordField passwordConfirmation = new PasswordField("Confirme a Senha");
	private final NativeButton signup = AgitterVaadinUtils.createDefaultNativeButton("Cadastrar");
	private final NativeButton cancel = AgitterVaadinUtils.createDefaultNativeButton("Voltar");

	SignupViewImpl(ComponentContainer container) {
		this.container = container;
	}

	void show() {
		container.removeAllComponents();
		container.addComponent(signupView); signupView.addStyleName("a-signup-view");
		
			// Welcome messages
			Label label = WidgetUtils.createLabel("Bem-vindo");
			signupView.addComponent(label); label.addStyleName("a-signup-welcome1");
			label = WidgetUtils.createLabel("Escolha uma senha e comece a agitar!");
			signupView.addComponent(label); label.addStyleName("a-signup-welcome2");
			// Signup Fields
			CssLayout signupfields = new CssLayout();
			signupView.addComponent(signupfields); signupfields.addStyleName("a-signup-fields");
				password.setDebugId("password");
				password.setSizeUndefined();
				signupfields.addComponent(password); password.addStyleName("a-signup-password");
				passwordConfirmation.setDebugId("passwordConfirmation");
				passwordConfirmation.setSizeUndefined();
				signupfields.addComponent(passwordConfirmation); passwordConfirmation.addStyleName("a-signup-password-confirmation");
				signupfields.addComponent(signup); signup.addStyleName("a-signup-button");
				signupfields.addComponent(cancel); cancel.addStyleName("a-signup-cancel-button");
		
		setupFocus();
		signup.setClickShortcut( KeyCode.ENTER );
		cancel.setClickShortcut( KeyCode.ESCAPE );
	}

	
	private void setupFocus() {
		WidgetUtils.focusOrder(password, passwordConfirmation, signup);
		password.focus();
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
	public void onCancel(final Runnable runnable) {
		cancel.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

}
