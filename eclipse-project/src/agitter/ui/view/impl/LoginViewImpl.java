package agitter.ui.view.impl;

import agitter.ui.view.LoginView;
import agitter.ui.view.SignupView;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

public class LoginViewImpl implements LoginView {

	private final VerticalLayout container;
	private final VerticalLayout loginView = new VerticalLayout();
	private final Button loginAgitterLogo = new NativeButton("Agitter!");
	private final GridLayout loginFieldsGrid = new GridLayout(3,2);
	private final TextField emailOrUsername = new TextField("Email ou Username");
	private final PasswordField password = new PasswordField("Senha");
	private final Button login = new NativeButton("Agitar!");
	private final Button forgotMyPassword = linkButton("Esqueci minha senha");
	private final Button signupLink = linkButton("Cadastre-se");
	private final ComponentContainer loginRightSideContainer = new VerticalLayout();

	LoginViewImpl(VerticalLayout container) {
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
		// Main Grid
		GridLayout loginMainGrid = new GridLayout(2,2); loginMainGrid.addStyleName("a-login-main-grid");
		loginView.addComponent(loginMainGrid);
		loginView.setComponentAlignment(loginMainGrid, Alignment.TOP_CENTER);
			// Logo
			loginAgitterLogo.addStyleName("a-login-agitter-logo");
			loginMainGrid.addComponent(loginAgitterLogo, 0, 0);
			// Login Fields and Buttons
			loginFieldsGrid.addStyleName("a-login-fields-grid");
			loginFieldsGrid.setSpacing(true);
			loginFieldsGrid.removeAllComponents();
			loginFieldsGrid.setVisible(true);
			loginMainGrid.addComponent(loginFieldsGrid, 1, 0);
			loginMainGrid.setComponentAlignment(loginFieldsGrid, Alignment.MIDDLE_LEFT);
				emailOrUsername.setWidth("170px");
				loginFieldsGrid.addComponent(emailOrUsername, 0, 0);
				password.setWidth("100%");  // To assume the same width as the forgotMyPassword button
				loginFieldsGrid.addComponent(password, 1, 0);
				loginFieldsGrid.addComponent(login, 2, 0);
				loginFieldsGrid.setComponentAlignment(login, Alignment.BOTTOM_CENTER);
				loginFieldsGrid.addComponent(forgotMyPassword, 1, 1);
			// Picture
			Embedded loginPicture = new Embedded(); loginPicture.addStyleName("a-login-picture");
			ThemeResource img = new ThemeResource("resource/login/LoginMainPicture.png");
			loginPicture.setSource(img);
			loginPicture.setType(Embedded.TYPE_IMAGE);
			loginMainGrid.addComponent(loginPicture, 0, 1);
			// RightSideContainer
			loginRightSideContainer.setWidth("430px");
			loginRightSideContainer.removeAllComponents();
			loginMainGrid.addComponent(loginRightSideContainer, 1, 1);
				// Advertisement
				VerticalLayout loginAdvertLayout = new VerticalLayout(); loginAdvertLayout.addStyleName("a-login-advert-layout");
				loginRightSideContainer.addComponent(loginAdvertLayout);
					Label label;
					label = new Label(
							"Ainda não está agitando?"
					);
					label.addStyleName("a-login-advert-1");
					loginAdvertLayout.addComponent(label);
					signupLink.addStyleName("a-login-advert-2");
					loginAdvertLayout.addComponent(signupLink);
					label = new Label(
							"Festas, saídas, jogos, churrascos, baladas,<br/> aniversários, espetáculos, qualquer coisa!"
					);
					label.setContentMode(Label.CONTENT_XHTML);
					label.addStyleName("a-login-advert-3");
					loginAdvertLayout.addComponent(label);
					label = new Label(
							"Saia da internet.<br/>Vá agitar com seus amigos!"
					);
					label.setContentMode(Label.CONTENT_XHTML);
					label.addStyleName("a-login-advert-4");
					loginAdvertLayout.addComponent(label);
		emailOrUsername.focus();
	}

	@Override
	public SignupView showSignupView(String email_, String suggestedUserName_, String password_) {
		loginFieldsGrid.setVisible(false);
		SignupView signup = new SignupViewImpl(loginRightSideContainer);
		signup.show(email_, suggestedUserName_, password_);
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
	public void onLoginAttempt(final Runnable runnable) {
		login.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
		signupLink.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
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

}
