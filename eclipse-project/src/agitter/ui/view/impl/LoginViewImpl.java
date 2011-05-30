package agitter.ui.view.impl;

import agitter.ui.view.AgitterTheme;
import agitter.ui.view.LoginView;
import agitter.ui.view.SignupView;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.BaseTheme;

public class LoginViewImpl implements LoginView {

	private final ComponentContainer container;
	private final CssLayout loginView = new CssLayout();
	private final CssLayout mainContent = new CssLayout();
	private final CssLayout topBar = new CssLayout();
	private final Button loginAgitterLogo = new NativeButton();
	private final CssLayout loginFields = new CssLayout();
	private final TextField emailOrUsername = new TextField("Email ou Username");
	private final PasswordField password = new PasswordField("Senha");
	private final Button login = new NativeButton("Agitar!");
	private final Button forgotMyPassword = linkButton("Esqueci minha senha");
	private final CssLayout loginRightSideContainer = new CssLayout();
	private final Button signup = new NativeButton("Cadastre-se");

	
	LoginViewImpl(ComponentContainer container) {
		this.container = container;
	}

	
	private Button linkButton(String caption) {
        Button b = new Button(caption);
        b.setStyleName(BaseTheme.BUTTON_LINK);
        return b;
	}

	
	@Override
	public void show() {
		container.removeAllComponents();
		loginView.addStyleName("a-login-view");
		loginView.removeAllComponents();
		container.addComponent(loginView); 
		// Main Content
		mainContent.removeAllComponents();
		loginView.addComponent(mainContent); mainContent.addStyleName("a-login-main-content");
			// Top Bar
			topBar.removeAllComponents();
			mainContent.addComponent(topBar); topBar.addStyleName("a-login-top-bar");
				topBar.addComponent(loginAgitterLogo); loginAgitterLogo.addStyleName("a-login-logo");
				loginAgitterLogo.addStyleName(AgitterTheme.DEFAULT_LOGO_COLOR_CLASS);
				loginAgitterLogo.setSizeUndefined();
				// Login Fields and Buttons
				loginFields.removeAllComponents();
				topBar.addComponent(loginFields); loginFields.addStyleName("a-login-fields");
					CssLayout wrapper = new CssLayout(); 
					loginFields.addComponent(wrapper); wrapper.addStyleName("a-login-username");
					emailOrUsername.setDebugId("username");
						emailOrUsername.setSizeUndefined();
						wrapper.addComponent(emailOrUsername);
					wrapper = new CssLayout();
					loginFields.addComponent(wrapper); wrapper.addStyleName("a-login-password");
						password.setDebugId("password");
						password.setSizeUndefined();
						wrapper.addComponent(password);
					loginFields.addComponent(login); login.addStyleName("a-login-button"); 
					login.addStyleName(AgitterTheme.DEFAULT_NATIVE_BUTTON_CLASS);
					loginFields.addComponent(forgotMyPassword); forgotMyPassword.addStyleName("a-login-forgot-password");
			// Picture
			Embedded loginPicture = new Embedded(); loginPicture.addStyleName("a-login-picture");
			ThemeResource img = new ThemeResource("login/LoginMainPicture.png");
			loginPicture.setSource(img);
			loginPicture.setType(Embedded.TYPE_IMAGE);
			mainContent.addComponent(loginPicture);
			// RightSideContainer
			loginRightSideContainer.removeAllComponents();
			mainContent.addComponent(loginRightSideContainer); loginRightSideContainer.addStyleName("a-login-rightside");
				// Advertisement
				Label label;
				label = new Label(
						"Ainda não está agitando?"
				);
				loginRightSideContainer.addComponent(label); label.addStyleName("a-login-advert-1");
				loginRightSideContainer.addComponent(signup); signup.addStyleName("a-login-signup-button");
				signup.addStyleName(AgitterTheme.DEFAULT_NATIVE_BUTTON_CLASS);
				label = new Label(
						"Festas, encontros, baladas, jogos,<br/>churrascos, espetáculos, qualquer coisa!"
				);
				label.setContentMode(Label.CONTENT_XHTML);
				loginRightSideContainer.addComponent(label); label.addStyleName("a-login-advert-3");
				label = new Label(
						"Saia da internet.<br/>Vá agitar com seus amigos!"
				);
				label.setContentMode(Label.CONTENT_XHTML);
				loginRightSideContainer.addComponent(label); label.addStyleName("a-login-advert-4");
									
		login.setClickShortcut( KeyCode.ENTER );
		
		setupFocus();
	}

	
	@Override
	public SignupView showSignupView() {
		topBar.removeComponent(loginFields);
		SignupView signup = new SignupViewImpl(loginRightSideContainer);
		signup.show();
		return signup;
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
	public void onLoginAttempt(final Runnable loginAction) {
		login.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			loginAction.run();
		}});
	}

	
	@Override
	public void onStartSignup(final Runnable action) {
		signup.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	
	@Override
	public void onForgotMyPassword(final Runnable runnable) {
		forgotMyPassword.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

	
	@Override
	public void onLogoClicked(final Runnable runnable) {
		loginAgitterLogo.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

	
	private void setupFocus() {
		VaadinUtils.focusOrder(emailOrUsername, password, login, forgotMyPassword, signup);
		emailOrUsername.focus();
	}

}

