package agitter.ui.view.session;

import agitter.ui.view.session.contacts.ContactsView;
import agitter.ui.view.session.events.EventsView;

public interface SessionView {

	void show(String username);

	@Deprecated
	EventsView eventsView();
	EventsView showEventsView();
	ContactsView showContactsView();
	
	void onLogout(Runnable onLogout);
	void onEventsMenu(Runnable onEventsMenu);
	void onContactsMenu(Runnable onContactsMenu);


}
