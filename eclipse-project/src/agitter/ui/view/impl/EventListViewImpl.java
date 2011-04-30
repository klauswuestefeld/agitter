package agitter.ui.view.impl;

import java.util.List;

import agitter.ui.view.EventData;
import agitter.ui.view.EventListView;

import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;

final class EventListViewImpl extends VerticalLayout implements EventListView {


	@Override
	public void display(List<EventData> events) {
		removeAllComponents();
		enableHiddenPolling();
		
		for (EventData eventData : events)
			addComponent(new EventViewImpl(eventData));
	}

	
	private void enableHiddenPolling() {
		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.setPollingInterval(1000 * 1);
		progressIndicator.setWidth("0px");
		progressIndicator.setHeight("0px");
		addComponent(progressIndicator);
	}

}