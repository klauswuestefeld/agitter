package agitter.ui.presenter;

import infra.util.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.AddressValidator;
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

		resetInviteView();

		_handle = SimpleTimer.runNowAndPeriodically(new Runnable() { @Override public void run() {
			refreshEventList();
		}});
	}

	
	private void resetInviteView() {
		inviteView().reset(contacts(), newInviteeValidator(), new Runnable() { @Override public void run() {
			invite();
		}});
	}

	
	private Predicate<String> newInviteeValidator() {
		return new Predicate<String>() { @Override public boolean evaluate(String newInvitee) {
			if (newInvitee == null) return false;
			try {
				AddressValidator.validateEmail(newInvitee);
			} catch (Refusal r) {
				_warningDisplayer.consume(r.getMessage());
				return false;
			}
			return true;
		}};
	}

	
	private List<String> contacts() {
		return ToString.toString(_contacts.all());
	}

	
	private void invite() {
		String description = inviteView().eventDescription();
		Date datetime = inviteView().datetime();
		List<String> inviteeStrings = inviteView().invitees();
		List<EmailAddress> invitees = toAddresses(inviteeStrings);
		try {
			validate(datetime);
			_events.create(_user, description, datetime.getTime(), Collections.EMPTY_LIST, invitees);
		} catch(Refusal e) {
			_warningDisplayer.consume(e.getMessage());
			return;
		}
		invitees.removeAll(_contacts.all());
		addNewContactsIfAny(invitees);
		
		refreshEventList();
		resetInviteView();
	}

	
	private void addNewContactsIfAny(List<EmailAddress> invitees) {
		for (EmailAddress contact : invitees)
			_contacts.addContact(contact);
	}

	
	private List<EmailAddress> toAddresses(List<String> inviteeStrings) {
		List<EmailAddress> result = new ArrayList<EmailAddress>(inviteeStrings.size());
		for (String string : inviteeStrings)
			result.add(toAddress(string));
		return result;
	}

	
	private EmailAddress toAddress(String validString) {
		try {
			return new EmailAddress(validString);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}


	private void validate(Date datetime) throws Refusal {
		if (datetime == null) throw new Refusal("Data do agito deve ser preenchida.");
	}


	private InviteView inviteView() {
		return _view.eventsView().inviteView();
	}


	synchronized
	private void refreshEventList() {
		_view.eventsView().eventListView().refresh(eventsToHappen(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
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
