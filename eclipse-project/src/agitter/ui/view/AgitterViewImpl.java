package agitter.ui.view;

import agitter.ui.view.authentication.AuthenticationView;
import agitter.ui.view.authentication.AuthenticationViewImpl;
import agitter.ui.view.session.SessionView;
import agitter.ui.view.session.SessionViewImpl;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.Window;

public class AgitterViewImpl extends com.vaadin.ui.Window implements AgitterView {

	private CssLayout agitterMainwindowContent = new CssLayout();
	public CssLayout innerView = new CssLayout();
	private final UriFragmentUtility fragmentUtility = new UriFragmentUtility();
	
	public AgitterViewImpl() {
		setCaption("agitter! :: Saia da Internet");
		addComponent(fragmentUtility);
		
		setContent(agitterMainwindowContent); agitterMainwindowContent.addStyleName("a-agitter-main-window");
		agitterMainwindowContent.addComponent(innerView); innerView.addStyleName("a-agitter-view");
	}

	
	@Override
	public void showWarningMessage(String message) {
		showNotification(message, Window.Notification.TYPE_WARNING_MESSAGE);
	}


	@Override
	public AuthenticationView authenticationView() {
		return new AuthenticationViewImpl(innerView);
	}
	

	@Override
	public SessionView showSessionView() {
		return new SessionViewImpl(innerView);
	}

	
	@Override
	public void setURIFragment(String fragment) {
		System.out.println("Fragment: " + fragment);
		fragmentUtility.setFragment(fragment);
	}


	@Override
	public void hideToAvoidBlinkAndRedirect(String url) {
		innerView.setVisible(false);
		open(new ExternalResource(url));
	}
	@Override
	public void show() {
		innerView.setVisible(true);
	}
	
}
