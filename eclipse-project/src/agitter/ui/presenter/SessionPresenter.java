package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.view.session.SessionView;

public class SessionPresenter {

	private final SessionView view;


	public SessionPresenter(User user, ContactsOfAUser contacts, Events events, SessionView view, Consumer<String> warningDisplayer, Runnable onLogout) {
		this.view = view;

		new EventsPresenter(user, contacts, events, view.eventsView(), warningDisplayer);
		new ContactsPresenter(contacts, view.contactsView(), warningDisplayer);

		view.onLogout(onLogout);
		view.onContactsMenu(onContactsMenu());
		view.onEventsMenu(onEventsMenu());
		
		view.show(user.username()); 
		view.showEventsView();

	}

	
	private Runnable onContactsMenu() {
		return new Runnable() {  @Override public void run() {
			view.showContactsView();
		} };
	}

	
	private Runnable onEventsMenu() {
		return new Runnable() {  @Override public void run() {
			view.showEventsView();
		}};
	}

}
