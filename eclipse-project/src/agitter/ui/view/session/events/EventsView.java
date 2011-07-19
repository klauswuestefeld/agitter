package agitter.ui.view.session.events;

import sneer.foundation.lang.Predicate;

public interface EventsView {

	void show();

	InviteView initInviteView(Predicate<String> newInviteeValidator, Runnable onInvite);
	EventListView eventListView();

}
