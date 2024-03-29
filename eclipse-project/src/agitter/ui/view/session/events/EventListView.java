package agitter.ui.view.session.events;

import java.util.List;

public interface EventListView {
	
	interface Boss {
		void onEventSelected(Object selectedEvent);
		void onEventRemoved(Object removedEvent, long datetime);
		void goingOnEvent(Object eventObject, long datetime);
		void mayGoToEvent(Object eventObject, long datetime);
		void onSearch(String fragment);
	}

	void startReportingTo(Boss boss);

	void setSelectedEvent(Object event);
	
	void refresh(List<EventVO> events, int millisToNextRefresh);

}
