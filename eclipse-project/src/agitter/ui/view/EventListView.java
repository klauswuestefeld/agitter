package agitter.ui.view;

import java.util.List;

public interface EventListView {

	void refresh(List<EventData> events, int millisToNextRefresh);
	
}
