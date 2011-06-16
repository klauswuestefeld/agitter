package agitter.domain.contacts;

import static infra.util.Collections.copy;
import static infra.util.ToString.containsToString;
import static infra.util.ToString.sortIgnoreCase;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public class ContactsOfAUserImpl implements ContactsOfAUser {

	private final List<EmailAddress> all = new ArrayList<EmailAddress>();
	private final List<Group> groups = new ArrayList<Group>();

	
	@Override
	public List<EmailAddress> all() {
		return copy(all);
	}

	
	@Override
	public void addContact(EmailAddress contact) {
		if (containsToString(all, contact)) return;
		all.add(contact);
		sortIgnoreCase(all);
	}

	
	@Override
	public void deleteContactAndRemoveFromAllGroups(EmailAddress contact) {
		all.remove(contact);
		for (Group group : groups)
			removeContactFrom(group, contact);
	}

	
	@Override
	public List<Group> groups() {
		return copy(groups);
	}


	@Override
	public void createGroup(String groupName) throws Refusal {
		GroupImpl newGroup = new GroupImpl(groupName);
		if (containsToString(groups(), newGroup))
			throw new Refusal("Já existe um grupo chamado " + groupName + ".");
		groups.add(newGroup);
		sortIgnoreCase(groups);
	}

	
	@Override
	public void renameGroup(Group group, String newName) throws Refusal {
		((GroupImpl)group).setName(newName);
		sortIgnoreCase(groups);
	}


	@Override
	public void addContactTo(Group group, EmailAddress contact) {
		addContact(contact);
		((GroupImpl)group).addMember(contact);
	}

	
	@Override
	public void removeContactFrom(Group group, EmailAddress contact) {
		((GroupImpl)group).removeMember(contact);
	}

}
