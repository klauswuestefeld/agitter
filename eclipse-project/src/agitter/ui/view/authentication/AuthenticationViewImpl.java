package agitter.ui.view.authentication;

import vaadinutils.WidgetUtils;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

public class AuthenticationViewImpl implements AuthenticationView {

	private final ComponentContainer container;
	private final CssLayout loginView = new CssLayout();
	private final CssLayout mainContent = new CssLayout();
	private final CssLayout topBar = new CssLayout();
	private final Button loginAgitterLogo = new NativeButton();
	private final CssLayout loginFields = new CssLayout();
	private final TextField email = new TextField("Email");
	private final Button enter = AgitterVaadinUtils.createDefaultNativeButton("Agitar!");
	private final CssLayout mainPicture = new CssLayout();
	private final CssLayout loginRightSideContainer = new CssLayout();

	
	public AuthenticationViewImpl(ComponentContainer container) {
		this.container = container;
	}

	
	@Override
	public void show() {
		container.removeAllComponents();
		loginView.addStyleName("a-auth-view");
		loginView.removeAllComponents();
		container.addComponent(loginView); 
		// Main Content
		mainContent.removeAllComponents();
		loginView.addComponent(mainContent); mainContent.addStyleName("a-auth-main-content");
			// Top Bar
			topBar.removeAllComponents();
			mainContent.addComponent(topBar); topBar.addStyleName("a-auth-top-bar");
				topBar.addComponent(loginAgitterLogo); loginAgitterLogo.addStyleName("a-auth-logo");
				loginAgitterLogo.setSizeUndefined();
				// Login Fields and Buttons
				loginFields.removeAllComponents();
				topBar.addComponent(loginFields); loginFields.addStyleName("a-auth-fields");
					loginFields.addComponent(email); email.addStyleName("a-auth-email");
						email.setDebugId("email");
						email.setSizeUndefined();
					loginFields.addComponent(enter); enter.addStyleName("a-auth-enter-button"); 
			// Picture
			mainContent.addComponent(mainPicture); mainPicture.addStyleName("a-auth-main-picture");
			// RightSideContainer
			loginRightSideContainer.removeAllComponents();
			mainPicture.addComponent(loginRightSideContainer); loginRightSideContainer.addStyleName("a-auth-rightside");
				// Advertisement
				loginRightSideContainer.addComponent(newLabel(
					"Ainda não está agitando?", "a-auth-advert-1"));
				loginRightSideContainer.addComponent(newLabel(
						"Festas, encontros, baladas, jogos,<br/>" +
						"churrascos, espetáculos, qualquer coisa.",
						"a-auth-advert-3"));
				loginRightSideContainer.addComponent(newLabel(
						"Convide seus amigos sem fazer spam.<br/>" +
						"Só quem estiver a fim recebe o convite ;)",
						"a-auth-advert-4"));
				loginRightSideContainer.addComponent(newLabel(
					"Saia da internet. Agite! \\o/",
					"a-auth-advert-4"));
									
		enter.setClickShortcut( KeyCode.ENTER );
		
		setupFocus();
	}


	private Label newLabel(String content, String style) {
		Label result = new Label(content);
		result.addStyleName(style);
		result.setContentMode(Label.CONTENT_XHTML);
		return result;
	}

	
	@Override
	public LoginView showLoginView() {
		topBar.removeComponent(loginFields);
		LoginViewImpl login = new LoginViewImpl(loginRightSideContainer);
		login.show();
		return login;
	}

	
	@Override
	public SignupView showSignupView() {
		topBar.removeComponent(loginFields);
		SignupViewImpl signup = new SignupViewImpl(loginRightSideContainer);
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

