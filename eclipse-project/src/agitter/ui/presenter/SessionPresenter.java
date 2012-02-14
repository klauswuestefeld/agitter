package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import agitter.domain.comments.Comments;
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
	private final EventsPresenter eventsPresenter;
	private final ContactsPresenter contactsPresenter;


	public SessionPresenter(User user, ContactsOfAUser contacts, Events events, Comments comments, Functor<EmailAddress, User> userProducer, SessionView view, Consumer<String> warningDisplayer, Runnable onLogout) {
		this.view = view;
		this.onLogout = onLogout;
		this.userScreenName = user.screenName();

		eventsPresenter = new EventsPresenter(user, contacts, events, comments, userProducer, view.eventsView(), warningDisplayer);
		contactsPresenter = new ContactsPresenter(contacts, view.contactsView(), userProducer, warningDisplayer);

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
		eventsPresenter.refreshContactsToChoose();
	}


	@Override
	public void onContactsMenu() {
		view.showContactsView();
		contactsPresenter.refresh();
	}

}
