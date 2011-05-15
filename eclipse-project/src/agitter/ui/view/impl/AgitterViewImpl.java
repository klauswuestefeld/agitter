package agitter.ui.view.impl;

import agitter.ui.view.AgitterView;
import agitter.ui.view.LoginView;
import agitter.ui.view.SessionView;

import com.vaadin.ui.Layout;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AgitterViewImpl extends com.vaadin.ui.Window implements AgitterView {

	private VerticalLayout mainPanel = new VerticalLayout();
	private final UriFragmentUtility fragmentUtility = new UriFragmentUtility();
	
	public AgitterViewImpl() {
		this.setCaption("Agitter!");
		this.getContent().addStyleName("a-agitter-main-window");
		((Layout)this.getContent()).setMargin(false);    // removes the margin from the entire application
		addComponent(fragmentUtility);
		mainPanel.addStyleName("a-agitter-view");
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
	public SessionView showSessionView() {
		return new SessionViewImpl(mainPanel);
	}

}
