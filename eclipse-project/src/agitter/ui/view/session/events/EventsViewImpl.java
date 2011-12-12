package agitter.ui.view.session.events;

import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public class EventsViewImpl implements EventsView {

	private final ComponentContainer container;
	private EventListViewImpl eventList;
	private EventViewImpl inviteView;
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
	public EventListView initEventListView() {
		if (eventList != null) throw new IllegalStateException();
		eventList = new EventListViewImpl();
		
		return eventList;
	}


	@Override
	public EventView showInviteView() {
		if (inviteView == null)
			inviteView = new EventViewImpl();
		return inviteView;
	}


	@Override
	public void onNewEvent(Runnable onNewEvent) {
		if (this.onNewEvent != null) throw new IllegalStateException();
		this.onNewEvent = onNewEvent;
	}

}
