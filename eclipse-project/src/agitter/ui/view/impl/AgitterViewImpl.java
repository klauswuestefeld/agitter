package agitter.ui.view.impl;

import agitter.ui.view.AgitterView;
import agitter.ui.view.LoginView;
import agitter.ui.view.SessionView;
import agitter.ui.view.SignupView;

import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AgitterViewImpl extends com.vaadin.ui.Window implements AgitterView {

	private VerticalLayout mainPanel = new VerticalLayout();
	private final UriFragmentUtility fragmentUtility = new UriFragmentUtility();
	
	public AgitterViewImpl() {
		addComponent(fragmentUtility);
		addComponent(mainPanel);
	}

	@Override
	public void showWarningMessage(String message) {
		showNotification(message, Window.Notification.TYPE_WARNING_MESSAGE);
	}


	@Override
	public LoginView loginView() {
		return new LoginViewImpl(mainPanel);
	}
	

	@Override
	public SignupView signupView() {
		return new SignupViewImpl(mainPanel);
	}


	@Override
	public SessionView showSessionView() {
		return new SessionViewImpl(mainPanel);
	}

}
