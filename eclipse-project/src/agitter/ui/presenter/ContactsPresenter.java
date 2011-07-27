package agitter.ui.presenter;

import java.util.List;

import agitter.domain.contacts.ContactsOfAUser;
import agitter.domain.contacts.Group;
import agitter.ui.view.session.contacts.ContactsView;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;

import static infra.util.ToString.toStrings;

public class ContactsPresenter {

	private final ContactsOfAUser contacts;
	private final ContactsView view;
	private final Consumer<String> warningDisplayer;

	public ContactsPresenter(final ContactsOfAUser contacts, final ContactsView view, final Consumer<String> warningDisplayer) {
		this.contacts = contacts;
		this.view = view;
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

		view.setMemberRemoveListener(new Consumer<String>() {
			@Override
			public void consume(String value) {
				System.err.println("MEMBER REMOVED: "+value);
			}
		});

		refreshMemberChoices();

		refreshGroupList();

	}
	private void refreshGroupList() {
		view.clearGroups();
		for(Group g : contacts.groups()) view.addGroup(g.name());
	}

	private void onGroupRemoved(String value) {
		System.err.println("GROUP REMOVED: "+value);
		contacts.deleteGroupAndRemoveFromAllContainingGroups(contacts.groupGivenName(value));
	}

	private void onGroupCreate(String groupName) {
		try {
			contacts.createGroup(groupName);
		} catch(Refusal refusal) {
			warningDisplayer.consume(refusal.getMessage());
			return;
		}
		refreshGroupList();
	}

	private void onGroupSelected(String value) {
		System.err.println("GROUP SELECTED: "+value);
		view.clearMembers();
		Group g = contacts.groupGivenName(value);
		List<String> members = toStrings(g.immediateMembers());
		members.addAll(toStrings(g.immediateSubgroups()));
		for(String m : members) view.addMember(m);
	}

	private void refreshMemberChoices() {
		List<String> allContactEmails = toStrings(contacts.all());
		allContactEmails.addAll(toStrings(contacts.groups()));
		view.setMembersToChoose(allContactEmails);
	}

}
