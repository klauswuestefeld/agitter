package agitter.ui.view.impl;

import agitter.ui.view.EventListView;
import agitter.ui.view.EventsView;
import agitter.ui.view.InviteView;

import com.vaadin.ui.ComponentContainer;

public class EventsViewImpl implements EventsView {

	private final ComponentContainer container;
	private final InviteViewImpl inviteView = new InviteViewImpl();
	private final EventListViewImpl eventList = new EventListViewImpl();


	public EventsViewImpl(ComponentContainer container) {
		this.container = container;
		this.container.addComponent(eventList);
		this.container.addComponent(inviteView);
	}


	@Override
	public InviteView inviteView() {
		return inviteView;
	}

	
	@Override
	public EventListView eventListView() {
		return eventList;
	}


}
