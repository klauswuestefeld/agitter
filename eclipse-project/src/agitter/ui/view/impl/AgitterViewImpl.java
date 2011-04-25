package agitter.ui.view.impl;


import agitter.ui.view.AgitterView;
import agitter.ui.view.EventListView;
import agitter.ui.view.InviteView;

import com.vaadin.ui.Window;

public class AgitterViewImpl extends com.vaadin.ui.Window implements AgitterView {

	private InviteViewImpl _invitePanel = new InviteViewImpl();
	private EventListViewImpl _eventList = new EventListViewImpl();

    public AgitterViewImpl(){
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
