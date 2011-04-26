package agitter.ui.view.impl;

import agitter.ui.view.AgitterView;
import agitter.ui.view.SessionView;

import com.vaadin.ui.Window;

public class AgitterViewImpl extends com.vaadin.ui.Window implements
		AgitterView {

	@Override
	public void showErrorMessage(String message) {
		showNotification(message, Window.Notification.TYPE_ERROR_MESSAGE);
	}

	
	@Override
	public SessionView showSessionView() {
		return new SessionViewImpl(this);
	}

}
