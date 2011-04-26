package agitter.ui.view.impl;


import agitter.ui.view.EventListView;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

import com.vaadin.ui.Window;

public class SessionViewImpl implements SessionView {

	private InviteViewImpl _invitePanel = new InviteViewImpl();
	private EventListViewImpl _eventList = new EventListViewImpl();

    public SessionViewImpl(Window window){
		window.addComponent(_invitePanel);
		window.addComponent(_eventList);
    }
    

	@Override
	public InviteView inviteView() {
		return _invitePanel;
	}

	
	@Override
	public EventListView eventListView() {
		return _eventList;
	}


}
