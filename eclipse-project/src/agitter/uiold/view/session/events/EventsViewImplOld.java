package agitter.uiold.view.session.events;


import com.vaadin.ui.ComponentContainer;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

public class EventsViewImplOld implements EventsView {

	private final ComponentContainer container;
	private EventListViewImpl eventList;
	private InviteViewImpl inviteView;


	public EventsViewImplOld(ComponentContainer container) {
		this.container = container;
	}


	@Override
	public void show() {
		container.removeAllComponents();
		container.addComponent(eventList);
		container.addComponent(inviteView);
	}

	@Override
	public EventListView initEventListView(Consumer<Object> selectedEventListener, Consumer<Object> removedEventListener) {
		if (eventList != null) throw new IllegalStateException();
		eventList = new EventListViewImpl(selectedEventListener, removedEventListener);
		return eventList;
	}


	@Override
	public InviteView initInviteView(Predicate<String> newInviteeValidator, Runnable onInvite) {
		if (inviteView != null) throw new IllegalStateException();
		inviteView = new InviteViewImpl(newInviteeValidator, onInvite);
		return inviteView;
	}


	@Override
	public void onNewEvent(Runnable onNewEvent) {
		throw new UnsupportedOperationException();
	}


}
