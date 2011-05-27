package agitter.ui.view.impl;

import java.util.List;

import agitter.ui.view.EventData;
import agitter.ui.view.EventListView;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.ProgressIndicator;

final class EventListViewImpl extends CssLayout implements EventListView {

	public EventListViewImpl() {
		addStyleName("a-event-list-view");
	}
	
	@Override
	public void refresh(List<EventData> events, int millisToNextRefresh) {
		removeAllComponents();
		addComponent(createPoller(millisToNextRefresh));
		
		for (EventData eventData : events)
			addComponent(new EventViewImpl(eventData));
	}

	private Component createPoller(int millisToNextRefresh) {
		ProgressIndicator result = new ProgressIndicator();
		result.setPollingInterval(millisToNextRefresh);
		result.setWidth("0px");
		result.setHeight("0px");
		return result;
	}

}