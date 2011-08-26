package agitter.ui.presenter;

import java.util.Arrays;
import java.util.List;

import sneer.foundation.lang.Consumer;
import agitter.ui.view.session.contacts.ContactsView;

public class ContactsDemoPresenter {

	private static final List<String> groupNames = Arrays.asList("Todos", "Amigos", "Familia", "Faculdade", "Trabalho");
	private static final List<String> memberNames = Arrays.asList("ana@gmail.com", "bob@yahoo.com.br", "carla@hotmail.com", "mario@nuintendo.com");

	public ContactsDemoPresenter(final ContactsView contactsView) {
		contactsView.setGroupSelectionListener(new Consumer<String>() {@Override public void consume(String value) {
			contactsView.setGroupSelected(value);
			System.err.println("GROUP SELECTED: " + value);
		}});
		contactsView.setGroupRemoveListener(new Consumer<String>() { @Override public void consume(String value) {
			System.err.println("GROUP REMOVED: " + value);
		}});
		contactsView.setMemberRemoveListener(new Consumer<String>() { @Override public void consume(String value) {
			System.err.println("MEMBER REMOVED: " + value);
		}});

		contactsView.setMembersToChoose(memberNames);
		contactsView.setGroups(groupNames);
		contactsView.setMembers(memberNames);
		
		contactsView.setGroupSelected("Todos");

	}

}
