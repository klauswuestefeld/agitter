package agitter.ui.presenter;

import static infra.util.ToString.sortIgnoreCase;
import static infra.util.ToString.toStrings;
import infra.util.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import agitter.ui.view.session.events.EventView;

public class InvitePresenter implements EventView.Boss {

	private final User user;
	private final ContactsOfAUser contacts;
	private final Events events;
	private final Consumer<String> warningDisplayer;
	private final Functor<EmailAddress, User> userProducer;
	private final EventView view;
	private final Runnable onDateOrDescriptionChanged;

	private Event selectedEvent = null;

	
	InvitePresenter(User user, ContactsOfAUser contacts, Events events, Functor<EmailAddress, User> userProducer, EventView view, Consumer<String> warningDisplayer, Runnable onDateOrDescriptionChanged) {
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
		refresh();
	}

	
	void clear() {
		setSelectedEvent(null);
	}

	
	private void refresh() {
		if (selectedEvent == null) {
			view.clear();
			return;
		}
		
		view.display(
			selectedEvent.description(),
			new Date(selectedEvent.datetime()),
			sortedKnownInviteesOf(selectedEvent), 
			countUnkownInviteesOf(selectedEvent)
		);

		refreshEditableMode();
	}


	private void refreshEditableMode() {
		boolean isEditable = events.isEditableBy(user, selectedEvent);
		
		view.editAll(false); // enableReadonlyfields
		view.enableEditListeners(isEditable); // enableListeners to edit each field independently. 
	}

	// force edit fields on everything. (Used for New Events) 
	public void editAll(boolean isEditting) {
		boolean isEditable = events.isEditableBy(user, selectedEvent);
		
		if (isEditable) {
			view.editAll(isEditting);
			if (isEditting) refreshFocus();
		}
	}

	private void refreshFocus() {
		if (selectedEvent.description().isEmpty())
			view.focusOnDate();
		else
			view.focusOnDescription();
	}

	private int countUnkownInviteesOf(Event event) {
		int cont = 0;
		for (User u : event.invitees()) {
			if (!contacts.isMyFriend(u.email())) { 
				cont ++;
			}
		}
		return cont;
	}

	private List<String> sortedKnownInviteesOf(Event event) {
		List<String> result = sortedGroupsInviteesOf(event);
		
		if (!event.owner().equals(user))
			result.add(event.owner().email().toString());
		
		List<String> userList = new ArrayList<String>();
		for (User u : event.invitees()) {
			if (contacts.isMyFriend(u.email())) { 
				userList.add(u.toString());
			}
		}
		
		sortIgnoreCase(userList);
		result.addAll(userList);
		
		return result;
	}

	private List<String> sortedInviteesOf(Event event) {
		List<String> result = sortedGroupsInviteesOf(event);
			
		if (!event.owner().equals(user))
			result.add(event.owner().email().toString());
		
		String[] users = toStrings(event.invitees());
		List<String> userList = Arrays.asList(users);
		sortIgnoreCase(userList);
		result.addAll(userList);
		
		return result;
	}
	
	private List<String> sortedGroupsInviteesOf(Event event) {
		List<String> result = new ArrayList<String>();
		String[] groups = toStrings(event.groupInvitees());
		result.addAll(Arrays.asList(groups));
		sortIgnoreCase(result);	
			
		return result;
	}
		
	void refreshContactsToChoose() {
		view.refreshInviteesToChoose(contacts());
	}


	@Override
	public void onDescriptionEdit(String newText) {
		if (newText != null &&  newText.trim().equals("")) {
			warningDisplayer.consume("Preencha a descrição do agito.");
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
		if (date == null) { //Unnecessary when we start using drop-downs instead of free-typing field 
			warningDisplayer.consume("Preencha a data do agito.");
			return;
		}
		
		try {
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


