package agitter.ui.view.session.events;

import java.util.List;

public interface EventListView {

	void refresh(List<EventValues> events, int millisToNextRefresh);

}
