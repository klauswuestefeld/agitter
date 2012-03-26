package agitter.ui.presenter;

import java.util.Arrays;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Pair;
import agitter.ui.view.session.contacts.ContactsView;

public class ContactsDemo {

	private static final List<String> groupNames = Arrays.asList("Todos", "Amigos", "Familia", "Faculdade", "Trabalho");
	private static final List<Pair<String,String>> memberPairs = 
			Arrays.asList(	new Pair<String,String>("ana@gmail.com", "Ana"), 
						  	new Pair<String,String>("bob@yahoo.com.br", "Bob"),
							new Pair<String,String>("carla@hotmail.com", "Carla"),
							new Pair<String,String>("mario@nuintendo.com", "Mario"));

	public ContactsDemo(final ContactsView contactsView) {
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

		contactsView.setMembersToChoose(memberPairs);
		contactsView.setGroups(groupNames);
		contactsView.setMembers(memberPairs);
		
		contactsView.setGroupSelected("Todos");
	}

}
