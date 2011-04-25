package agitter.ui.view.impl;

import java.util.List;

import agitter.ui.view.EventData;
import agitter.ui.view.EventListView;

import com.vaadin.ui.VerticalLayout;

final class EventListViewImpl extends VerticalLayout implements EventListView {

	@Override
	public void display(List<EventData> events) {
		removeAllComponents();
		for (EventData eventData : events)
			addComponent(new EventViewImpl(eventData));
	}

}