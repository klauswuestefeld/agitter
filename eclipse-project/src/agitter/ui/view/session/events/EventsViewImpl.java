package agitter.ui.view.session.events;


import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

import com.vaadin.ui.ComponentContainer;

public class EventsViewImpl implements EventsView {

	private final ComponentContainer container;
	private EventListViewImpl eventList;
	private InviteViewImpl inviteView;


	public EventsViewImpl(ComponentContainer container) {
		this.container = container;
	}


	@Override
	public void show() {
		container.removeAllComponents();
		container.addComponent(eventList);
		container.addComponent(inviteView);
	}

	@Override
	public EventListView initEventListView(Consumer<Long> selectedEventIdListener) {
		if(eventList!=null) throw new IllegalStateException();
		eventList = new EventListViewImpl(selectedEventIdListener);
		return eventList;
	}


	@Override
	public InviteView initInviteView(Predicate<String> newInviteeValidator, Runnable onInvite) {
		if (inviteView != null) throw new IllegalStateException();
		inviteView = new InviteViewImpl(newInviteeValidator, onInvite);
		return inviteView;
	}


}
