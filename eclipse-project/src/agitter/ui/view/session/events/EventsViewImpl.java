package agitter.ui.view.session.events;

import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;

public class EventsViewImpl implements EventsView {

	private final ComponentContainer container;
	private EventListViewImpl eventList;
	private EventViewImpl inviteView;
	private Runnable onNewEvent;
	private Boss boss;


	public EventsViewImpl(ComponentContainer container) {
		this.container = container;
	}


	@Override
	public void show() {
		container.removeAllComponents();
		

		TextField search = new TextField();
		search.setInputPrompt("Search");
		search.setImmediate(true);
		search.setTextChangeEventMode(TextChangeEventMode.LAZY);
		search.setTextChangeTimeout(500);
		search.addListener(new TextChangeListener() { @Override public void textChange(TextChangeEvent event) {
			boss.onSearch(event.getText());
		}});

		CssLayout leftSide = new CssLayout();
		container.addComponent(leftSide); leftSide.addStyleName("a-events-view-left-side");
			CssLayout newEventWrapper = new CssLayout();
			leftSide.addComponent(newEventWrapper); newEventWrapper.addStyleName("a-events-view-new-event-wrapper");
				Button newEvent = AgitterVaadinUtils.createDefaultNativeButton("Criar novo Agito");
				newEventWrapper.addComponent(newEvent); newEvent.addStyleName("a-events-view-new-event-button");
				leftSide.addComponent(search);
			leftSide.addComponent(eventList);
		container.addComponent((EventViewImpl)inviteView());
		
		newEvent.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			if (onNewEvent != null) onNewEvent.run();
		}});
		
	}
	
	@Override
	public void startRepontingTo(Boss boss) {
		this.boss = boss;
	}


	@Override
	public EventListView initEventListView() {
		if (eventList != null) throw new IllegalStateException();
		eventList = new EventListViewImpl();
		
		return eventList;
	}


	@Override
	public EventView inviteView() {
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
