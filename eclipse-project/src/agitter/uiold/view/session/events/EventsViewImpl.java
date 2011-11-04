package agitter.uiold.view.session.events;


import agitter.ui.view.AgitterVaadinUtils;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

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

		ComponentContainer leftSide = new VerticalLayout();
		leftSide.addComponent(createNewEventButton());
		leftSide.addComponent(eventList);

		ComponentContainer peccinPleaseRemoveMe = new HorizontalLayout();
		peccinPleaseRemoveMe.addComponent(leftSide);
		peccinPleaseRemoveMe.addComponent(inviteView);

		container.addComponent(peccinPleaseRemoveMe);
	}


	private NativeButton createNewEventButton() {
		NativeButton result = AgitterVaadinUtils.createDefaultNativeButton("Agitar!");
		result.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			if (onNewEvent != null) onNewEvent.run();
		}});
		return result;
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
