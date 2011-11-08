package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.view.session.SessionView;
import agitter.ui.view.session.SessionView.Needs;

public class SessionPresenter implements Needs {

	private final SessionView view;
	private final Runnable onLogout;
	private final String userScreenName;


	public SessionPresenter(User user, ContactsOfAUser contacts, Events events, Functor<EmailAddress, User> userProducer, SessionView view, Consumer<String> warningDisplayer, Runnable onLogout) {
		this.view = view;
		this.onLogout = onLogout;
		this.userScreenName = user.screenName();

		new EventsPresenter(user, contacts, events, userProducer, view.eventsView(), warningDisplayer);
		new ContactsPresenter(contacts, view.contactsView(), userProducer, warningDisplayer);

		view.init(this);
		view.showEventsView();
	}

	
	@Override
	public String userScreenName() {
		return userScreenName;
	}


	@Override
	public void onLogout() {
		onLogout.run();
	}


	@Override
	public void onEventsMenu() {
		view.showEventsView();
	}


	@Override
	public void onContactsMenu() {
		view.showContactsView();
	}

}
