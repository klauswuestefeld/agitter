package agitter.ui.presenter;

import javax.servlet.http.HttpSession;

import basis.lang.Consumer;
import basis.lang.Functor;

import agitter.common.Portal;
import agitter.controller.oauth.OAuth;
import agitter.domain.comments.Comments;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.view.session.SessionView;
import agitter.ui.view.session.SessionView.Boss;

public class SessionPresenter implements Boss {

	private final User loggedUser;
	private final SessionView view;
	private final Runnable onLogout;
	private final EventsPresenter eventsPresenter;
	private final ContactsPresenter contactsPresenter;
	private final AccountPresenter accountPresenter;

	public SessionPresenter(User user, ContactsOfAUser contacts, Events events, Comments comments, Functor<EmailAddress, User> userProducer, SessionView view, Consumer<String> warningDisplayer, Runnable onLogout, OAuth oAuth, HttpSession httpSession, String context, Consumer<String> urlRedirector, Notifier<String> urlRestPathNotifier) {
		this.loggedUser = user;
		this.view = view;
		this.onLogout = onLogout;

		eventsPresenter = new EventsPresenter(user, contacts, events, comments, userProducer, view.eventsView(), warningDisplayer, urlRestPathNotifier);
		contactsPresenter = new ContactsPresenter(contacts, view.contactsView(), userProducer, warningDisplayer);
		accountPresenter = new AccountPresenter(user, view.accountView(), oAuth, warningDisplayer, httpSession, context, urlRedirector);
		
		contactsPresenter.setUpdateFriendsListener(new Consumer<Portal>() { @Override public void consume(Portal value) {
			accountPresenter.onUpdateFriends(value);
		}});

		eventsPresenter.setUpdateContactsListener(new Runnable() { @Override public void run() {
       		 onAccountMenu();
		}});

		
		view.startReportingTo(this);
		view.setUserScreenName(loggedUser.screenName());
		view.showEventsView();
	}

	
	public User loggedUser() {
		return loggedUser;
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


	@Override
	public void onAccountMenu() {
		view.showAccountView();
		accountPresenter.refresh();
	}

	public void refresh() {
		eventsPresenter.refreshContactsToChoose();
		contactsPresenter.refresh();
		accountPresenter.refresh();
	}
	
}
