package agitter.domain.contacts;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;

public interface ContactsOfAUser {

	List<User> all();
	void addContact(User contact);
	void deleteContactAndRemoveFromAllGroups(User contact);

	boolean isMyFriend(User user);
	void addAll(ContactsOfAUser contactsOf) throws Refusal;

	List<Group> groups();
	@Transaction
	Group createGroup(String groupName) throws Refusal;
	void renameGroup(Group group, String newName) throws Refusal;
	void deleteGroupAndRemoveFromAllContainingGroups(Group group);
	void addContactTo(Group group, User contact);
	void removeContactFrom(Group group, User contact);
	Group groupGivenName(String name);
	
}
