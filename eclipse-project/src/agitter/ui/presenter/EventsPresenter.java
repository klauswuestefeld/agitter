package agitter.ui.presenter;

import static agitter.domain.emails.EmailAddress.mail;
import infra.util.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.Predicate;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.emails.AddressValidator;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.presenter.SimpleTimer.HandleToAvoidLeaks;
import agitter.ui.view.session.events.EventData;
import agitter.ui.view.session.events.EventsView;
import agitter.ui.view.session.events.InviteView;

public class EventsPresenter {

	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Consumer<String> warningDisplayer;
	private final EventsView view;
	private InviteView inviteView;

	@SuppressWarnings("unused")
	private final HandleToAvoidLeaks handle;
	private final Functor<EmailAddress, User> userSearch;

	public EventsPresenter(User user, ContactsOfAUser contacts, Events events, Functor<EmailAddress, User> userSearch, EventsView eventsView, Consumer<String> warningDisplayer) {
		this.user = user;
		this.contacts = contacts;
		this.events = events;
		this.userSearch = userSearch;
		this.view = eventsView;
		this.warningDisplayer = warningDisplayer;

		resetInviteView();

		handle = SimpleTimer.runNowAndPeriodically(new Runnable() { @Override public void run() {
			refreshEventList();
		}});
	}

	private void resetInviteView() {
		inviteView().reset(contacts());
	}

	private Predicate<String> newInviteeValidator() {
		return new Predicate<String>() { @Override public boolean evaluate(String newInvitee) {
			if(newInvitee==null) { return false; }
			try {
				AddressValidator.validateEmail(newInvitee);
			} catch(Refusal r) {
				warningDisplayer.consume(r.getMessage());
				return false;
			}
			return true;
		}};
	}

	private List<String> contacts() {
		return ToString.toStrings(contacts.all());
	}

	private void invite() {
		String description = inviteView().eventDescription();
		Date datetime = inviteView().datetime();
		List<User> invitees = toUsers(inviteView().invitees());
		try {
			validate(datetime);
			events.create(user, description, datetime.getTime(), Collections.EMPTY_LIST, invitees);
		} catch(Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		invitees.removeAll(contacts.all());
		addNewContactsIfAny(invitees);

		refreshEventList();
		resetInviteView();
	}


	private void addNewContactsIfAny(List<User> invitees) {
		for(User contact : invitees) { contacts.addContact(contact); }
	}


	private List<User> toUsers(List<String> validEmails) {
		List<User> result = new ArrayList<User>(validEmails.size());
		for(String email : validEmails)
			result.add(userSearch.evaluate(toAddress(email)));
		return result;
	}

	private EmailAddress toAddress(String validEmail) {
		try {
			return mail(validEmail);
		} catch(Refusal e) {
			throw new IllegalStateException(e);
		}
	}

	private void validate(Date datetime) throws Refusal {
		if(datetime==null) { throw new Refusal("Data do agito deve ser preenchida."); }
	}

	private InviteView inviteView() {
		if(inviteView==null) {
			inviteView = view.initInviteView(newInviteeValidator(), new Runnable() { @Override public void run() {
				invite();
			}});
		}
		return inviteView;
	}

	synchronized private void refreshEventList() {
		view.eventListView().refresh(eventsToHappen(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
	}

	private List<EventData> eventsToHappen() {
		List<EventData> result = new ArrayList<EventData>();
		List<Event> toHappen = events.toHappen(user);
		for(Event event : toHappen) {
			result.add(new EventData(event.description(), event.datetime(), event.owner().screenName(), removeAction(event)));
		}
		return result;
	}


	private Runnable removeAction(final Event event) {
		if(event.owner().equals(user)) { return null; }

		return new Runnable() { @Override public void run() {
			event.notInterested(user);
			refreshEventList();
		}};
	}

}
