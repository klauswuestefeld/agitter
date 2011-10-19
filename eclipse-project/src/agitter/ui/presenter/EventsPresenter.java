package agitter.ui.presenter;

import static agitter.domain.emails.EmailAddress.email;
import static infra.util.ToString.sortIgnoreCase;
import static infra.util.ToString.toStrings;
import infra.util.ToString;

import java.util.ArrayList;
import java.util.Arrays;
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
import agitter.ui.view.session.events.EventListView;
import agitter.ui.view.session.events.EventVO;
import agitter.ui.view.session.events.EventsView;
import agitter.ui.view.session.events.InviteView;

public class EventsPresenter {

	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Consumer<String> warningDisplayer;
	private final EventsView view;
	private InviteView inviteView;
	private EventListView eventListView;

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
			return email(validEmail);
		} catch(Refusal e) {
			throw new IllegalStateException(e);
		}
	}

	
	private void validate(Date datetime) throws Refusal {
		if(datetime==null) { throw new Refusal("Data do agito deve ser preenchida."); }
	}

	
	private InviteView inviteView() {
		if (inviteView == null)
			inviteView = view.initInviteView(newInviteeValidator(), new Runnable() { @Override public void run() {
				invite();
			}});

		return inviteView;
	}

	
	synchronized private void refreshEventList() {
		eventsListView().refresh(eventsToHappen(), SimpleTimer.MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
	}

	
	private EventListView eventsListView() {
		if (eventListView == null)
			eventListView = view.initEventListView(new Consumer<Object>() { @Override public void consume(Object event) {
				onEventSelected((Event)event);
			}}, new Consumer<Object>() { @Override public void consume(Object event) {
				onEventRemoved((Event)event);
			}});

		return eventListView;
	}

	
	private void onEventSelected(Event event) {
		inviteView().display(event.description(), new Date(event.datetime()), sortedInviteesOf(event));
	}

	
	private List<String> sortedInviteesOf(Event event) {
		List<String> result = new ArrayList<String>();

		String[] groups = toStrings(event.groupInvitees());
		result.addAll(Arrays.asList(groups));
		sortIgnoreCase(result);
		
		String[] users = toStrings(event.invitees());
		List<String> userList = Arrays.asList(users);
		sortIgnoreCase(userList);
		result.addAll(userList);
		
		return result;
	}


	private List<EventVO> eventsToHappen() {
		List<EventVO> result = new ArrayList<EventVO>();
		List<Event> toHappen = events.toHappen(user);
		for (Event event : toHappen)
			result.add(new EventVO(event, event.description(), event.datetime(), event.owner().screenName(), isDeletable(event)));
		return result;
	}

	
	private boolean isDeletable(Event event) {
		return events.isDeletableBy(event, user);
	}


	private void onEventRemoved(Event event) {
		if (isDeletable(event))
			events.delete(event, user);
		else
			event.notInterested(user);
		refreshEventList();
	}

}
