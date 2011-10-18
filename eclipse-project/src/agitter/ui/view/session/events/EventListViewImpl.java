package agitter.ui.view.session.events;

import java.util.List;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.ProgressIndicator;
import sneer.foundation.lang.Consumer;

final class EventListViewImpl extends CssLayout implements EventListView {

	public EventListViewImpl(final Consumer<Long> selectedEventIdListener) {
		addStyleName("a-event-list-view");
		this.addListener(new LayoutEvents.LayoutClickListener() { @Override public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
			EventViewImpl childComponent = (EventViewImpl) layoutClickEvent.getChildComponent();
			if(childComponent==null) return;
			Long id = childComponent.getEventId();
			selectedEventIdListener.consume(id);
		}});
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

}