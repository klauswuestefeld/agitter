package agitter.ui.view.session.events;

import java.util.List;

public interface EventListView {

	void refresh(List<EventVO> events, int millisToNextRefresh);

}
