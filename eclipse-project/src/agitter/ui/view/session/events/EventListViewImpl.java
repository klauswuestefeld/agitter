package agitter.ui.view.session.events;

import java.util.List;

import sneer.foundation.lang.Consumer;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.ProgressIndicator;

final class EventListViewImpl extends CssLayout implements EventListView {

	private final Consumer<Long> selectedEventIdListener;


	EventListViewImpl(final Consumer<Long> selectedEventIdListener) {
		this.selectedEventIdListener = selectedEventIdListener;
		this.addListener(new LayoutEvents.LayoutClickListener() { @Override public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
			onEventSelected(layoutClickEvent);
		}});
		addStyleName("a-event-list-view");
	}

	
	@Override
	public void refresh(List<EventData> events, int millisToNextRefresh) {
		removeAllComponents();
		addComponent(createPoller(millisToNextRefresh));

		for(EventData eventData : events) addComponent(new EventViewImpl(eventData));
	}

	
	private Component createPoller(int millisToNextRefresh) {
		ProgressIndicator result = new ProgressIndicator();
		result.setPollingInterval(millisToNextRefresh);
		result.setWidth("0px");
		result.setHeight("0px");
		return result;
	}

	
	private void onEventSelected(LayoutClickEvent layoutClickEvent) {
		EventViewImpl eventView = (EventViewImpl) layoutClickEvent.getChildComponent();
		if (eventView == null) return;
		Long id = eventView.eventId();
		selectedEventIdListener.consume(id);
	}
	
}