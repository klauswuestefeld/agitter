package agitter.ui.view.session.events;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public class EventsViewImpl implements EventsView {

	private final ComponentContainer container;
	private EventListViewImpl eventList;
	private InviteViewImpl inviteView;
	private Runnable onNewEvent;


	public EventsViewImpl(ComponentContainer container) {
		this.container = container;
	}


	@Override
	public void show() {
		container.removeAllComponents();
		
		CssLayout leftSide = new CssLayout();
		container.addComponent(leftSide); leftSide.addStyleName("a-events-view-left-side");
			CssLayout newEventWrapper = new CssLayout();
			leftSide.addComponent(newEventWrapper); newEventWrapper.addStyleName("a-events-view-new-event-wrapper");
				Button newEvent = AgitterVaadinUtils.createDefaultNativeButton("Criar novo Agito");
				newEventWrapper.addComponent(newEvent); newEvent.addStyleName("a-events-view-new-event-button");
			leftSide.addComponent(eventList);
		container.addComponent(inviteView);
		
		newEvent.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			if (onNewEvent != null) onNewEvent.run();
		}});
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
		if (this.onNewEvent != null) throw new IllegalStateException();
		this.onNewEvent = onNewEvent;
	}

}
