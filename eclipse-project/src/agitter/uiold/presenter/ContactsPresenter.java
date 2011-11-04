package agitter.uiold.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.domain.emails.AddressValidator;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.ui.view.session.contacts.ContactsView;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.Predicate;
import sneer.foundation.lang.exceptions.Refusal;

import static infra.util.ToString.toStrings;

public class ContactsPresenter {

	private final ContactsOfAUser contacts;
	private final ContactsView view;
	private final Consumer<String> warningDisplayer;
	private Group groupSelected;
	private final Functor<EmailAddress, User> userSearch;
	
	public ContactsPresenter(final ContactsOfAUser contacts, final ContactsView view, Functor<EmailAddress, User> userSearch, final Consumer<String> warningDisplayer) {
		this.contacts = contacts;
		this.view = view;
		this.userSearch = userSearch;
		this.warningDisplayer = warningDisplayer;
		view.setGroupCreateListener(new Consumer<String>() { @Override public void consume(String value) {
			onGroupCreate(value);
		}});

		view.setGroupSelectionListener(new Consumer<String>() { @Override public void consume(String value) {
			onGroupSelected(value);
		}});

		view.setGroupRemoveListener(new Consumer<String>() { @Override public void consume(String value) {
			onGroupRemoved(value);
		}});

		view.setMemberEntryListener(memberEntryValidator(), new Consumer<String>() { @Override public void consume(String value) {
			onMemberEntered(value);
		}});

		view.setMemberRemoveListener(new Consumer<String>() { @Override public void consume(String value) {
			onMemberRemoved(value);
		}});

		refreshGroupList();
		
		onGroupSelected(null);

	}


	private void refreshGroupList() {
		view.setGroups(toStrings(contacts.groups()));
	}

	
	private void onGroupRemoved(String value) {
		contacts.deleteGroupAndRemoveFromAllContainingGroups(findGroup(value));
		refreshGroupList();
		onGroupSelected(null);
	}

	
	private void onGroupCreate(String groupName) {
		try {
			contacts.createGroup(groupName);
		} catch(Refusal refusal) {
			warningDisplayer.consume(refusal.getMessage());
			return;
		}
		view.clearGroupCreateField();
		refreshGroupList();
		onGroupSelected(groupName);
	}

	
	private void onGroupSelected(String value) {
		view.setGroupSelected(value);
		groupSelected = findGroup(value);
		refreshMemberList();
	}

	
	private void refreshMemberList() {
		refreshMemberChoices();

		final List<User> memberUsers;
		final List<Group> subgroups;
		if (isAllContactsGroupSelected()) {
			memberUsers = contacts.all();
			subgroups = Collections.EMPTY_LIST;
		} else {
			memberUsers = groupSelected.immediateMembers();
			subgroups = groupSelected.immediateSubgroups();
		}	
		List<String> members = toStrings(subgroups);
		members.addAll(toStrings(memberUsers));
		
		view.setMembers(members);
	}

	
	private void refreshMemberChoices() {
		if (isAllContactsGroupSelected()) {
			view.setMembersToChoose(Collections.EMPTY_LIST);
			return;
		}
		
		List<Object> choices = new ArrayList<Object>();

		choices.addAll(contacts.groups());
		choices.remove(groupSelected);
		choices.removeAll(groupSelected.immediateSubgroups());
		
		choices.addAll(contacts.all());
		choices.removeAll(groupSelected.immediateMembers());
		
		view.setMembersToChoose(toStrings(choices));
	}

	
	private Predicate<String> memberEntryValidator() {
		return new Predicate<String>() { @Override public boolean evaluate(String entry) {
			if (isValidEmailEntry(entry)) return true;
			if (isValidGroupEntry(entry)) return true;
			
			String message = isAllContactsGroupSelected()
				? "Digite um Email válido."
				: "Digite um Email válido ou um Grupo da lista.";
			
			warningDisplayer.consume(message);
			refreshMemberChoices();
			return false;
		}};
	}

	
	private boolean isValidGroupEntry(String entry) {
		if (isAllContactsGroupSelected()) return false;
		return contacts.groupGivenName(entry) != null;
	}

	
	private boolean isValidEmailEntry(String entry) {
		return AddressValidator.isValidEmail(entry);
	}


	private void onMemberEntered(String value) {
		if (isAllContactsGroupSelected())
			contacts.addContact(produceUser(value));
		else {
			Group group = contacts.groupGivenName(value);
			if (group != null)
				onMemberEntered(group);
			else {
				User user = produceUser(value);
				contacts.addContactTo(groupSelected, user);
			}
		}
		refreshMemberList();
	}

	
	private void onMemberEntered(Group group) {
		groupSelected.addSubgroup(group);
	}
	
	
	private void onMemberRemoved(String value) {
		removeMember(value);
		refreshMemberList();
	}

	
	private void removeMember(String value) {
		if (isAllContactsGroupSelected()) {
			contacts.deleteContactAndRemoveFromAllGroups(produceUser(value));
			return;
		}
		
		Group group = contacts.groupGivenName(value);
		if (group != null)
			groupSelected.removeSubgroup(group);
		else {
			User user = produceUser(value);
			contacts.removeContactFrom(groupSelected, user);
		}
	}
	
	
	private User produceUser(String value) {
		try {
			return userSearch.evaluate(EmailAddress.email(value));
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

	
	private boolean isAllContactsGroupSelected() {
		return groupSelected == null;
	}


	private Group findGroup(String groupName) {
		if (groupName == null) return null;
		Group result = contacts.groupGivenName(groupName);
		if (result == null) throw new IllegalArgumentException("Group not found: " + groupName);
		return result;
	}

}

