package agitter.domain.contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public class GroupImpl implements Group {
	
	private String name;
	
	private SortedSet<EmailAddress> contacts = new TreeSet<EmailAddress>();
	
	private SortedSet<Group> subgroups = new TreeSet<Group>();

	public GroupImpl(String groupName) {
		name = groupName;
	}
	
	@Override
	public List<EmailAddress> contacts() {
		return new ArrayList<EmailAddress>(contacts);
	}
	
	@Override
	public void addContact(EmailAddress emailAddress) {
		contacts.add(emailAddress);
	}
	
	@Override
	public List<Group> subgroups() {
		return new ArrayList<Group>( subgroups );
	}
	
	@Override
	public Group addSubgroup(String subGroupName) throws Refusal {
		GroupImpl subgroup = new GroupImpl(subGroupName);
		boolean subgroupAdded = subgroups.add(subgroup);
		if(!subgroupAdded) 
			throw new Refusal("Já existe um grupo chamado " + subGroupName);

		return subgroup;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public int compareTo(Group other) {
		return name().compareToIgnoreCase( other.name() );
	}

	@Override
	public String toString() {
		return name();
	}
	
}
