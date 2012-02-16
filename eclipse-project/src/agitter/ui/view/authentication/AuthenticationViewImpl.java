package agitter.ui.view.authentication;

import vaadinutils.WidgetUtils;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

public class AuthenticationViewImpl implements AuthenticationView {

	private final ComponentContainer container;
	private final Button loginAgitterLogo = new NativeButton();
	private final TextField email = new TextField();
	private final Button enter = AgitterVaadinUtils.createDefaultNativeButton("Entrar");
	private final Button facebook = new NativeButton();
	private final Button twitter = new NativeButton();
	private final Button google = new NativeButton();
	private final Button windows = new NativeButton();
	private final Button yahoo = new NativeButton();
	private final CssLayout popup = new CssLayout();
	private Label socialLabel = WidgetUtils.createLabel("Entre via Rede Social");
	private Label emailLabel = WidgetUtils.createLabel("Ou digite seu e-mail");

	
	public AuthenticationViewImpl(ComponentContainer container) {
		this.container = container;
	}

	
	@Override
	public void show() {
		container.removeAllComponents();
		CssLayout innerLayout = new CssLayout();
		innerLayout.addStyleName("a-auth-view");
		container.addComponent(innerLayout); 
			// Top Bar
			CssLayout topBar = new CssLayout();
			innerLayout.addComponent(topBar); topBar.addStyleName("a-auth-topbar");
			CssLayout topBarContent = new CssLayout();
			topBar.addComponent(topBarContent); topBarContent.addStyleName("a-auth-topbar-content");
				topBarContent.addComponent(loginAgitterLogo); loginAgitterLogo.addStyleName("a-auth-topbar-logo");
				loginAgitterLogo.setSizeUndefined();
				CssLayout topBarRight = new CssLayout();
				topBarContent.addComponent(topBarRight); topBarRight.addStyleName("a-auth-topbar-right");
					// Social Buttons
					CssLayout socialButtons = new CssLayout();
					topBarRight.addComponent(socialButtons); socialButtons.addStyleName("a-auth-topbar-social");
						socialButtons.addComponent(socialLabel); socialLabel.addStyleName("a-auth-topbar-social-label");
						socialButtons.addComponent(google); google.addStyleName("a-auth-topbar-social-button"); google.addStyleName("a-auth-topbar-social-google");
						socialButtons.addComponent(windows); windows.addStyleName("a-auth-topbar-social-button"); windows.addStyleName("a-auth-topbar-social-windows");
						socialButtons.addComponent(yahoo); yahoo.addStyleName("a-auth-topbar-social-button"); yahoo.addStyleName("a-auth-topbar-social-yahoo");
						socialButtons.addComponent(facebook); facebook.addStyleName("a-auth-topbar-social-button"); facebook.addStyleName("a-auth-topbar-social-facebook");
						socialButtons.addComponent(twitter); twitter.addStyleName("a-auth-topbar-social-button"); twitter.addStyleName("a-auth-topbar-social-twitter");
						google.setDescription("Entrar via Google");
						windows.setDescription("Entrar via WindowsLive, MSN, Hotmail");
						yahoo.setDescription("Entrar via Yahoo");
						facebook.setDescription("Entrar via Facebook");
						twitter.setDescription("Twitter: Em breve!");
					// Login Fields and Buttons
					CssLayout loginFields = new CssLayout();
					topBarRight.addComponent(loginFields); loginFields.addStyleName("a-auth-topbar-fields");
						loginFields.addComponent(emailLabel); socialLabel.addStyleName("a-auth-topbar-email-label");
						loginFields.addComponent(email); email.addStyleName("a-auth-topbar-email");
							email.setDebugId("email");
							email.setInputPrompt("Digite seu Email");
							email.setSizeUndefined();
						loginFields.addComponent(enter); enter.addStyleName("a-auth-topbar-enter-button");
							enter.setDebugId("enter");
			// Main content
			CssLayout popupContainer = new CssLayout();
			innerLayout.addComponent(popupContainer); popupContainer.addStyleName("a-auth-main");
				popup.removeAllComponents();
				popupContainer.addComponent(popup); popup.addStyleName("a-auth-main-content");
									
		setModalEnablement(true);
		
		setupFocus();
	}
	
	private String getJavascriptElement(Component component) {
		return "document.getElementById('" + component.getDebugId() + "')";
	}
	
	@Override
	public String getRetryAuthenticationJavascript() {
		String enterElement = getJavascriptElement(enter);
		return enterElement + ".focus();" + enterElement + ".click();";
	}

	@Override
	public LoginView showLoginView() {
		setModalEnablement(false);
		LoginViewImpl login = new LoginViewImpl(popup);
		login.show();
		return login;
	}


	@Override
	public SignupView showSignupView() {
		setModalEnablement(false);
		SignupViewImpl signup = new SignupViewImpl(popup);
		signup.show(email());
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

	
	@Override
	public void onGoogleSignin(final Runnable action) {
		google.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}
	
	
	@Override
	public void onWindowsSignin(final Runnable action) {
		windows.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}
	
	
	@Override
	public void onYahooSignin(final Runnable action) {
		yahoo.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}
	
	
	@Override
	public void onFacebookSignin(final Runnable action) {
		facebook.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}	

	
	@Override
	public void onTwitterSignin(final Runnable action) {
//		TODO Resolver a questão do email pelo Twitter		
//		twitter.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
//			action.run();
//		}});
	}	
	
	private void setModalEnablement(boolean enabled) {
		if (enabled)
			enter.setClickShortcut(KeyCode.ENTER);
		else
			enter.removeClickShortcut();

		emailLabel.setEnabled(enabled);
		email.setEnabled(enabled);
		enter.setEnabled(enabled);
		socialLabel.setEnabled(enabled);
		facebook.setEnabled(enabled);
		twitter.setEnabled(enabled);
		google.setEnabled(enabled);
		windows.setEnabled(enabled);
		yahoo.setEnabled(enabled);
	}

	
	private void setupFocus() {
		WidgetUtils.focusOrder(email, enter);
		email.focus();
	}

}

