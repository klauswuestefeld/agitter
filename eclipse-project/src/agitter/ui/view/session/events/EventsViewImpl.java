package agitter.ui.view.session.events;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import vaadinutils.WidgetUtils;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

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
			leftSide.addComponent(createNewEventButton());
			leftSide.addComponent(eventList);

		container.addComponent(inviteView);
	}


	private Component createNewEventButton() {
		//NativeButton result = AgitterVaadinUtils.createDefaultNativeButton("Agitar!");
		Label newEventLabel = WidgetUtils.createLabel("Criar um Agito");
		CssLayout newEventLayout = new CssLayout();
		newEventLayout.addStyleName("a-events-view-new-event");
		
		newEventLayout.addComponent(newEventLabel);
		newEventLayout.addListener(new LayoutEvents.LayoutClickListener() { @Override public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
			if (onNewEvent != null) onNewEvent.run();
		}});
		
		return newEventLayout;
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
