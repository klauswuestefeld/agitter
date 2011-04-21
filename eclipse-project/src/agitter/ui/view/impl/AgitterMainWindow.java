package agitter.ui.view.impl;


import agitter.ui.presenter.Presenter;
import agitter.ui.view.AgitterView;
import agitter.ui.view.EventListView;
import agitter.ui.view.InviteView;

import com.vaadin.ui.Window;

public class AgitterMainWindow extends com.vaadin.ui.Window implements AgitterView {

	private InvitePanel _invitePanel = new InvitePanel();
	private EventListViewImplementation _eventList = new EventListViewImplementation();

    public AgitterMainWindow(Presenter presenter){
    	presenter.startSession(this);
		addComponent(_invitePanel);
		addComponent(_eventList);
    }
    

	@Override
	public InviteView inviteView() {
		return _invitePanel;
	}

	
	@Override
	public EventListView eventListView() {
		return _eventList;
	}

	
	@Override
	public void showErrorMessage(String message) {
		showNotification(message, Window.Notification.TYPE_ERROR_MESSAGE);
	}
	
}
