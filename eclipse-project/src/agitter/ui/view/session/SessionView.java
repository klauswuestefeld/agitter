package agitter.ui.view.session;

import agitter.ui.view.session.account.AccountView;
import agitter.ui.view.session.contacts.ContactsView;
import agitter.ui.view.session.events.EventsView;

public interface SessionView {
	
	public interface Boss {
		void onLogout();
		void onEventsMenu();
		void onContactsMenu();
		void onAccountMenu();
	}
	
	void startReportingTo(Boss boss);
	
	void setUserScreenName(String screenName);

	EventsView eventsView();
	void showEventsView();

	ContactsView contactsView();
	void showContactsView();

	AccountView accountView();
	void showAccountView();

}
