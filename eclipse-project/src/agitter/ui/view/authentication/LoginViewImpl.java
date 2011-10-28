package agitter.ui.view.authentication;

import vaadinutils.WidgetUtils;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;

public class LoginViewImpl implements LoginView {

	private final ComponentContainer container;
	private final CssLayout loginView = new CssLayout();
	private final PasswordField password = new PasswordField("Senha");
	private final Button forgotMyPassword = WidgetUtils.createLinkButton("Esqueci minha senha");
	private final CheckBox keepMeLoggedIn = new CheckBox("Mantenha-me conectado");
	private final NativeButton login = AgitterVaadinUtils.createDefaultNativeButton("Entrar");
	private final NativeButton cancel = AgitterVaadinUtils.createDefaultNativeButton("Voltar");

	LoginViewImpl(ComponentContainer container) {
		this.container = container;
	}

	void show() {
		container.removeAllComponents();
		container.addComponent(loginView); loginView.addStyleName("a-login-view");
		
			// Welcome messages
			Label label = WidgetUtils.createLabel("Bem-vindo!");
			loginView.addComponent(label); label.addStyleName("a-login-welcome1");
			// Signup Fields
			CssLayout loginfields = new CssLayout();
			loginView.addComponent(loginfields); loginfields.addStyleName("a-login-fields");
				password.setDebugId("password");
				password.setSizeUndefined();
				loginfields.addComponent(password); password.addStyleName("a-login-password");
				loginfields.addComponent(login); login.addStyleName("a-login-button");
				loginfields.addComponent(cancel); cancel.addStyleName("a-login-cancel-button");
				loginfields.addComponent(forgotMyPassword); forgotMyPassword.addStyleName("a-login-forgot-password");
		
		setupFocus();
		login.setClickShortcut( KeyCode.ENTER );
	}

	
	private void setupFocus() {
		WidgetUtils.focusOrder(password, forgotMyPassword, keepMeLoggedIn, login);
		password.focus();
	}

	@Override
	public String password() {
		return (String)password.getValue(); 
	}

	@Override
	public void onForgotMyPassword(final Runnable runnable) {
		forgotMyPassword.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

	@Override
	public void onCancel(final Runnable runnable) {
		cancel.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

	@Override
	public boolean keepMeLoggedIn() {
		return (Boolean)keepMeLoggedIn.getValue(); 
	}

	@Override
	public void onLoginAttempt(final Runnable runnable) {
		login.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

}
