package agitter.ui.view.impl;

import vaadinutils.VaadinUtils;
import agitter.ui.view.AgitterTheme;
import agitter.ui.view.LoginView;
import agitter.ui.view.SignupView;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
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
	private final Button login = AgitterTheme.createDefaultNativeButton("Agitar!");
	private final Button forgotMyPassword = linkButton("Esqueci minha senha");
	private final CssLayout mainPicture = new CssLayout();
	private final CssLayout loginRightSideContainer = new CssLayout();
	private final Button signup = AgitterTheme.createDefaultNativeButton("Cadastre-se");

	
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
					loginFields.addComponent(forgotMyPassword); forgotMyPassword.addStyleName("a-login-forgot-password");
			// Picture
			mainContent.addComponent(mainPicture); mainPicture.addStyleName("a-login-main-picture");
			// RightSideContainer
			loginRightSideContainer.removeAllComponents();
			mainContent.addComponent(loginRightSideContainer); loginRightSideContainer.addStyleName("a-login-rightside");
				// Advertisement
				loginRightSideContainer.addComponent(newLabel(
					"Ainda não está agitando?", "a-login-advert-1"));
				loginRightSideContainer.addComponent(signup); signup.addStyleName("a-login-signup-button");
				loginRightSideContainer.addComponent(newLabel(
						"Festas, encontros, baladas, jogos,<br/>" +
						"churrascos, espetáculos, qualquer coisa.",
						"a-login-advert-3"));
				loginRightSideContainer.addComponent(newLabel(
						"Convide seus amigos sem fazer spam.<br/>" +
						"Só quem estiver a fim recebe o convite ;)",
						"a-login-advert-4"));
				loginRightSideContainer.addComponent(newLabel(
					"Saia da internet. Agite! \\o/",
					"a-login-advert-4"));
									
		login.setClickShortcut( KeyCode.ENTER );
		
		setupFocus();
	}


	private Label newLabel(String content, String style) {
		Label result = new Label(content);
		result.addStyleName(style);
		result.setContentMode(Label.CONTENT_XHTML);
		return result;
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

