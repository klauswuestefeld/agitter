package agitter.ui.view.authentication;

import vaadinutils.WidgetUtils;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

public class AuthenticationViewImpl implements AuthenticationView {

	private final ComponentContainer container;
	private final CssLayout loginView = new CssLayout();
	private final CssLayout topBarContent = new CssLayout();
	private final CssLayout topBar = new CssLayout();
	private final Button loginAgitterLogo = new NativeButton();
	private final CssLayout loginFields = new CssLayout();
	private final TextField email = new TextField("Digite seu Email");
	private final Button enter = AgitterVaadinUtils.createDefaultNativeButton("Entrar");
	private final CssLayout main = new CssLayout();
	private final CssLayout mainContent = new CssLayout();

	
	public AuthenticationViewImpl(ComponentContainer container) {
		this.container = container;
	}

	
	@Override
	public void show() {
		container.removeAllComponents();
		loginView.addStyleName("a-auth-view");
		loginView.removeAllComponents();
		container.addComponent(loginView); 
			// Top Bar
			topBar.removeAllComponents();
			loginView.addComponent(topBar); topBar.addStyleName("a-auth-topbar");
			topBarContent.removeAllComponents();
			topBar.addComponent(topBarContent); topBarContent.addStyleName("a-auth-topbar-content");
				topBarContent.addComponent(loginAgitterLogo); loginAgitterLogo.addStyleName("a-auth-topbar-logo");
				loginAgitterLogo.setSizeUndefined();
				// Login Fields and Buttons
				loginFields.removeAllComponents();
				topBarContent.addComponent(loginFields); loginFields.addStyleName("a-auth-topbar-fields");
					loginFields.addComponent(email); email.addStyleName("a-auth-topbar-email");
						email.setEnabled(true);
						email.setDebugId("email");
						email.setInputPrompt("Digite seu Email");
						email.setSizeUndefined();
					loginFields.addComponent(enter); enter.addStyleName("a-auth-topbar-enter-button");
						enter.setEnabled(true);

			// Main content
			loginView.addComponent(main); main.addStyleName("a-auth-main");
				mainContent.removeAllComponents();
				main.addComponent(mainContent); mainContent.addStyleName("a-auth-main-content");
									
		enter.setClickShortcut( KeyCode.ENTER );
		
		setupFocus();
	}


	@Override
	public LoginView showLoginView() {
		email.setEnabled(false);
		enter.setEnabled(false);
		LoginViewImpl login = new LoginViewImpl(mainContent);
		login.show();
		return login;
	}

	
	@Override
	public SignupView showSignupView() {
		email.setEnabled(false);
		enter.setEnabled(false);
		SignupViewImpl signup = new SignupViewImpl(mainContent);
		signup.show();
		return signup;
	}

	
	@Override
	public String email() {
		return (String)email.getValue();
	}

	
	@Override
	public void onEnterAttempt(final Runnable loginAction) {
		enter.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			loginAction.run();
		}});
	}

	
	@Override
	public void onLogoClicked(final Runnable runnable) {
		loginAgitterLogo.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

	
	private void setupFocus() {
		WidgetUtils.focusOrder(email, enter);
		email.focus();
	}

}

