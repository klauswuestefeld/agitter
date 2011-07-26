package agitter.ui.presenter;

import java.util.Arrays;
import java.util.List;

import agitter.ui.view.session.contacts.ContactsView;
import sneer.foundation.lang.Consumer;

public class ContactsDemoPresenter {

	private static final List<String> groupNames = Arrays.asList("Amigos", "Familia", "Faculdade", "Trabalho");
	private static final List<String> memberNames = Arrays.asList("ana@gmail.com", "bob@yahoo.com.br", "carla@hotmail.com");

	public ContactsDemoPresenter(ContactsView contactsView) {
		contactsView.setGroupSelectionListener(new Consumer<String>() {@Override public void consume(String value) {
			System.err.println("GROUP SELECTED: " + value);
		}});
		contactsView.setGroupRemoveListener(new Consumer<String>() { @Override public void consume(String value) {
			System.err.println("GROUP REMOVED: " + value);
		}});
		contactsView.setMemberRemoveListener(new Consumer<String>() { @Override public void consume(String value) {
			System.err.println("MEMBER REMOVED: " + value);
		}});

		contactsView.setMembersToChoose(memberNames);
		for(String g : groupNames) contactsView.addGroup(g);
		for(String m : memberNames) contactsView.addMember(m);

	}

}
