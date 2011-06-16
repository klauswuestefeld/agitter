package agitter.domain.contacts;

import static infra.util.Collections.copy;
import static infra.util.ToString.sortIgnoreCase;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public class GroupImpl implements Group {
	
	private String name;
	private List<EmailAddress> members = new ArrayList<EmailAddress>();
	private List<Group> subgroups = new ArrayList<Group>();

	
	public GroupImpl(String groupName) throws Refusal {
		setName(groupName);
	}

	
	private void validateGroupName(String groupName) throws Refusal {
		if( groupName == null || groupName.trim().isEmpty() )
			throw new Refusal( "O nome do grupo deve ser preenchido." );
	}

	
	@Override
	public List<EmailAddress> immediateContacts() {
		return copy(members);
	}
	
	
	void addContact(EmailAddress emailAddress) {
		members.add(emailAddress);
		sortIgnoreCase(members);
	}
	
	
	@Override
	public List<Group> immediateSubgroups() {
		return copy(subgroups);
	}
	
	
	@Override
	public String name() {
		return name;
	}

	
	@Override
	public String toString() {
		return name();
	}

	
	@Override
	public boolean deepContains(EmailAddress mail) {
		ArrayList<Group> visited = new ArrayList<Group>();
		return deepContains(mail, visited);
	}

	
	private boolean deepContains(EmailAddress mail, ArrayList<Group> visited) {
		if (visited.contains(this)) return false;
		visited.add(this);
		if (members.contains(mail)) return true;
		for (Group subgroup : subgroups)
			if (((GroupImpl)subgroup).deepContains(mail, visited))
				return true;
		return false;
	}


	void setName(String newName) throws Refusal {
		validateGroupName(newName);
		name = newName;
	}

	
	@Override
	public void addSubgroup(Group subgroup) {
		subgroups.add(subgroup);
		sortIgnoreCase(subgroups);
	}

	
	@Override
	public void removeSubgroup(Group subgroup) {
		subgroups.remove(subgroup);
	}
	
}
