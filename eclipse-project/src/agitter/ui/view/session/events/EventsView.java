package agitter.ui.view.session.events;

import sneer.foundation.lang.Consumer;

public interface EventsView {

	void show();

	void onNewEvent(Runnable onNewEvent);
	EventView showInviteView();
	EventListView initEventListView(Consumer<Object> selectedEventListener, Consumer<Object> removedEventListener);

}
