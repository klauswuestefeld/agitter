package agitter.ui.view.session.events;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

public interface EventsView {

	void show();

	InviteView initInviteView(Predicate<String> newInviteeValidator, Runnable onInvite);
	EventListView initEventListView(Consumer<Object> selectedEventListener, Consumer<Object> removedEventListener);

}
