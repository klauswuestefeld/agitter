package agitter.ui.view.session.events;

import java.util.List;

import agitter.domain.events.Event;

public interface EventListView {
	
	interface Boss {
		void onEventSelected(Object selectedEvent);
		void onEventRemoved(Object removedEvent, long datetime);
		void goingOnEvent(Object eventObject, long datetime);
		void mayGoOnEvent(Object eventObject, long datetime);
	}

	void startReportingTo(Boss boss);

	void setSelectedEvent(Event event);
	
	void refresh(List<EventVO> events, int millisToNextRefresh);

}
