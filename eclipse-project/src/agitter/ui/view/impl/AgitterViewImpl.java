package agitter.ui.view.impl;

import agitter.ui.view.AgitterView;
import agitter.ui.view.LoginView;
import agitter.ui.view.SessionView;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.Window;

public class AgitterViewImpl extends com.vaadin.ui.Window implements AgitterView {

	private CssLayout agitterMainwindowContent = new CssLayout();
	private CssLayout agitterView = new CssLayout();
	private final UriFragmentUtility fragmentUtility = new UriFragmentUtility();
	
	public AgitterViewImpl() {
		this.setCaption("agitter! :: Saia da Internet");
		setContent(agitterMainwindowContent); agitterMainwindowContent.addStyleName("a-agitter-main-window");
		agitterMainwindowContent.addComponent(fragmentUtility);
		agitterMainwindowContent.addComponent(agitterView); agitterView.addStyleName("a-agitter-view");
	}

	@Override
	public void showWarningMessage(String message) {
		showNotification(message, Window.Notification.TYPE_WARNING_MESSAGE);
	}


	@Override
	public LoginView loginView() {
		return new LoginViewImpl(agitterView);
	}
	

	@Override
	public SessionView showSessionView() {
		return new SessionViewImpl(agitterView);
	}

}
