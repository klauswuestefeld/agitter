package agitter.uiold.view.session;

import agitter.ui.view.session.contacts.ContactsView;
import agitter.ui.view.session.events.EventsView;

public interface SessionView {
	
	public interface Needs {
		String userScreenName();
		void onLogout();
		void onEventsMenu();
		void onContactsMenu();
	}
	
	void init(Needs needs);
	
	EventsView eventsView();
	void showEventsView();

	ContactsView contactsView();
	void showContactsView();

}
