package agitter.ui.view.session.events;


import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

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
		
		ComponentContainer leftSide = new VerticalLayout();
		leftSide.addComponent(AgitterVaadinUtils.createDefaultNativeButton("Agitar!"));
		leftSide.addComponent(eventList);

		ComponentContainer peccinPleaseRemoveMe = new HorizontalLayout();
		peccinPleaseRemoveMe.addComponent(leftSide);
		peccinPleaseRemoveMe.addComponent(inviteView);

		container.addComponent(peccinPleaseRemoveMe);
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


}
