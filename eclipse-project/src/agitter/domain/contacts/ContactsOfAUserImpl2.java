package agitter.domain.contacts;

import static infra.util.Collections.copy;
import static infra.util.ToString.containsToString;
import static infra.util.ToString.sortIgnoreCase;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

public class ContactsOfAUserImpl2 implements ContactsOfAUser {

	private final List<User> all = new ArrayList<User>();
	private final List<Group> groups = new ArrayList<Group>();

	
	@Override
	public List<User> all() {
		return copy(all);
	}

	
	@Override
	public void addContact(User contact) {
		if (containsToString(all, contact)) return;
		all.add(contact);
		sortIgnoreCase(all);
	}

	
	@Override
	public void deleteContactAndRemoveFromAllGroups(User contact) {
		all.remove(contact);
		for (Group group : groups)
			removeContactFrom(group, contact);
	}

	
	@Override
	public List<Group> groups() {
		return copy(groups);
	}


	@Override
	public Group createGroup(String groupName) throws Refusal {
		GroupImpl2 newGroup = new GroupImpl2(groupName);
		if (containsToString(groups(), newGroup))
			throw new Refusal("Já existe um grupo chamado " + groupName + ".");
		groups.add(newGroup);
		sortIgnoreCase(groups);
		return newGroup;
	}
	@Override
	public void renameGroup(Group group, String newName) throws Refusal {
		((GroupImpl2)group).setName(newName);
		sortIgnoreCase(groups);
	}
	@Override
	public void deleteGroupAndRemoveFromAllContainingGroups(Group contained) {
		groups.remove(contained);
		for (Group containing : groups)
			containing.removeSubgroup(contained);
	}
	@Override
	public Group groupGivenName(String name) {
		for(Group g : groups)
			if(g.name().equals(name))
				return g;
		return null;
	}


	@Override
	public void addContactTo(Group group, User contact) {
		addContact(contact);
		((GroupImpl2)group).addMember(contact);
	}

	
	@Override
	public void removeContactFrom(Group group, User contact) {
		((GroupImpl2)group).removeMember(contact);
	}


	@Override
	public boolean isMyFriend(EmailAddress email) {
		for (User u : all()) {
			if (u.email().equals(email)) {
				return true;
			}
		}
		return false;
	}

}
