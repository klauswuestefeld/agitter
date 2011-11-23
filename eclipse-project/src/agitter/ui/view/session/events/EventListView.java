package agitter.ui.view.session.events;

import java.util.List;

public interface EventListView {
	
	interface Boss {
		void onEventSelected(Object selectedEvent);
		void onEventRemoved(Object removedEvent);
	}

	void startReportingTo(Boss boss);
	
	void refresh(List<EventVO> events, int millisToNextRefresh);

}
