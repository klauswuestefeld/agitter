package agitter.ui.view.session.events;


import com.vaadin.ui.ComponentContainer;

public class EventsViewImpl implements EventsView {

	private final ComponentContainer container;
	private final InviteViewImpl inviteView = new InviteViewImpl();
	private final EventListViewImpl eventList = new EventListViewImpl();


	public EventsViewImpl(ComponentContainer container) {
		this.container = container;
		show();
	}


	@Override
	public void show() {
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
