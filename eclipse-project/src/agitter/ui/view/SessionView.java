package agitter.ui.view;

public interface SessionView {

	EventsView eventsView();

	void show(String username);
	
	ContactsView showContactsView();
	
	void onLogout(Runnable onLogout);
	void onEventsMenu(Runnable onEventsMenu);
	void onContactsMenu(Runnable onContactsMenu);

}
