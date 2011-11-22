package agitter.ui.presenter;

import static infra.util.ToString.sortIgnoreCase;
import static infra.util.ToString.toStrings;
import infra.util.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.emails.AddressValidator;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import agitter.domain.events.Events;
import agitter.domain.users.User;
import agitter.ui.view.session.events.InviteView;

public class InvitePresenter implements InviteView.Boss {

	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Consumer<String> warningDisplayer;
	private final Functor<EmailAddress, User> userProducer;
	private final InviteView view;
	private final Runnable onDateOrDescriptionChanged;

	private boolean isNewEventBeingCreated = false;
	private Event selectedEvent = null;

	
	InvitePresenter(User user, ContactsOfAUser contacts, Events events, Functor<EmailAddress, User> userProducer, InviteView view, Consumer<String> warningDisplayer, Runnable onDateOrDescriptionChanged) {
		this.user = user;
		this.contacts = contacts;
		this.events = events;
		this.userProducer = userProducer;
		this.view = view;
		this.warningDisplayer = warningDisplayer;
		this.onDateOrDescriptionChanged = onDateOrDescriptionChanged;

		this.view.startReportingTo(this);
		
		refreshContactsToChoose();
		refresh();
	}

	
	void setSelectedEvent(Event event) {
		selectedEvent = event;
		isNewEventBeingCreated = false;
		refresh();
	}

	
	void startCreatingNewEvent() {
		selectedEvent = null;
		isNewEventBeingCreated = true;
		refresh();
	}


	void clear() {
		selectedEvent = null;
		isNewEventBeingCreated = false;
		refresh();
	}

	
	private void refresh() {
		if (isNewEventBeingCreated) {
			view.display("", new Date(Clock.currentTimeMillis()), Collections.EMPTY_LIST);
			view.enableEdit(true);
			return;
		}
		if (selectedEvent != null) {
			view.display(selectedEvent.description(), new Date(selectedEvent.datetime()), sortedInviteesOf(selectedEvent));
			view.enableEdit(events.isEditableBy(user, selectedEvent));
			return;
		}
		view.clear();
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


	void refreshContactsToChoose() {
		view.refreshInviteesToChoose(contacts());
	}


	@Override
	public void onDescriptionEdit(String newText) {
		if (isNewEventBeingCreated) {
			warningDisplayer.consume("Preencha a data do agito.");
			return;
		}
			
		try {
			events.setDescription(user, selectedEvent, newText);
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		onDateOrDescriptionChanged.run();
	}


	@Override
	public void onDatetimeEdit(Date date) {
		if (date == null) {
			warningDisplayer.consume("Preencha a data do agito.");
			return;
		}
		
		try {
			if (isNewEventBeingCreated) {
				Event event = events.create(user, "", date.getTime());
				setSelectedEvent(event);
			}
			events.setDatetime(user, selectedEvent, date.getTime());
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		onDateOrDescriptionChanged.run();
	}


	@Override
	public boolean approveInviteeAdd(String invitee) {
		if (invitee == null) return false;

		if (selectedEvent == null) {
			warningDisplayer.consume("Preencha a data e a descrição do agito.");
			return false;
		}

		Object inviteeObj = toGroupOrUser(invitee);
		if (inviteeObj == null) return false;
		
		if (inviteeObj instanceof Group)
			selectedEvent.addInvitee((Group)inviteeObj);
		else {
			selectedEvent.addInvitee((User)inviteeObj);
			contacts.addContact((User)inviteeObj);
		}

		return true;
	}


	private Object toGroupOrUser(String invitee) {
		Object result = contacts.groupGivenName(invitee);
		if (result != null) return result;

		if (AddressValidator.isValidEmail(invitee))
			return userProducer.evaluate(EmailAddress.certain(invitee));
			
		warningDisplayer.consume("Grupo ou email invalido: " + invitee);
		return null;
	}


	@Override
	public void onInviteeRemoved(String invitee) {
		Object inviteeObj = toGroupOrUser(invitee);
		if (inviteeObj == null) return;
		if (inviteeObj instanceof Group)
			selectedEvent.removeInvitee((Group)inviteeObj);
		else
			selectedEvent.removeInvitee((User)inviteeObj);
	}

	
	private List<String> contacts() {
		List<String> contactsAndGroups = ToString.toStrings(contacts.groups());
		contactsAndGroups.addAll(ToString.toStrings(contacts.all()));
		return contactsAndGroups;
	}
}


