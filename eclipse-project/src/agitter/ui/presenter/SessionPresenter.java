package agitter.ui.presenter;

import infra.util.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.presenter.SimpleTimer.HandleToAvoidLeaks;
import agitter.ui.view.EventData;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

public class SessionPresenter {

	private final User _user;
	private final ContactsOfAUser _contacts;
	private final Events _events;
	private final SessionView _view;
	private final Consumer<String> _warningDisplayer;

	@SuppressWarnings("unused")
	private final HandleToAvoidLeaks _handle;


	public SessionPresenter(User user, ContactsOfAUser contacts, Events events, SessionView view, Consumer<String> errorDisplayer, Runnable onLogout) {
		_user = user;
		_contacts = contacts;
		_events = events;
		_view = view;
		_warningDisplayer = errorDisplayer;

		_view.onLogout(onLogout);
		
		_view.show(_user.username()); 

		resetView();
		inviteView().onInvite(new Runnable() { @Override public void run() {
			invite();
		}});

		_handle = SimpleTimer.runNowAndPeriodically(new Runnable() { @Override public void run() {
			refreshEventList();
		}});
	}

	private void resetView() {
		inviteView().clearFields();
		inviteView().setContacts(contacts());
	}

	private List<String> contacts() {
		return ToString.toString(_contacts.all());
	}

	private void invite() {
		String description = inviteView().getEventDescription();
		Date datetime = inviteView().getDatetime();
		List<String> invitations = inviteView().invitations();
		try {
			validate(datetime);
			int convertInvitationsToEmailAddressList;
			_events.create(_user, description, datetime.getTime(), invitations);
			addContacts(invitations);
		} catch(Refusal e) {
			_warningDisplayer.consume(e.getMessage());
			return;
		}
		refreshEventList();
		resetView();
	}

	private void addContacts(List<String> invitations) throws Refusal {
		for (String string : invitations)
			_contacts.addContact(new EmailAddress(string));
	}


	private void validate(Date datetime) throws Refusal {
		if(datetime==null) { throw new Refusal("Data do agito deve ser preenchida."); }
	}


	private InviteView inviteView() {
		return _view.inviteView();
	}


	synchronized
	private void refreshEventList() {
		_view.eventListView().refresh(eventsToHappen(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
	}


	private List<EventData> eventsToHappen() {
		List<EventData> result = new ArrayList<EventData>();
		List<Event> toHappen = _events.toHappen(_user);
		for(Event event : toHappen) {
			result.add(new EventData(
				event.description(),
				event.datetime(),
				event.owner().username(),
				removeAction(event)
			));
		}
		return result;
	}


	private Runnable removeAction(final Event event) {
		if (event.owner().equals(_user)) return null;
		
		return new Runnable() { @Override public void run() {
			event.notInterested(_user);
			refreshEventList();
		}};
	}

}

