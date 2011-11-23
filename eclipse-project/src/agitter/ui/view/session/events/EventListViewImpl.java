package agitter.ui.view.session.events;

import java.util.List;

import sneer.foundation.lang.Consumer;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.ProgressIndicator;

final class EventListViewImpl extends CssLayout implements EventListView {

	private final Consumer<Object> selectedEventListener;
	private final Consumer<Object> removedEventListener;


	EventListViewImpl(Consumer<Object> selectedEventListener, Consumer<Object> removedEventListener) {
		this.selectedEventListener = selectedEventListener;
		this.removedEventListener = removedEventListener;
		this.addListener(new LayoutEvents.LayoutClickListener() { @Override public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
			onEventSelected(layoutClickEvent);
		}});
		addStyleName("a-event-list-view");
	}

	
	@Override
	public void refresh(List<EventVO> events, int millisToNextRefresh) {
		removeAllComponents();
		addComponent(createPoller(millisToNextRefresh));

		for (EventVO eventData : events)
			addComponent(new EventListElement(eventData, removedEventListener));
	}

	
	private Component createPoller(int millisToNextRefresh) {
		ProgressIndicator result = new ProgressIndicator();
		result.setPollingInterval(millisToNextRefresh);
		result.setWidth("0px");
		result.setHeight("0px");
		return result;
	}

	
	private void onEventSelected(LayoutClickEvent layoutClickEvent) {
		EventListElement eventView = (EventListElement) layoutClickEvent.getChildComponent();
		if (eventView == null) return;
		Object eventObject = eventView.getEventObject();
		selectedEventListener.consume(eventObject);
	}
	
}