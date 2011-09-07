package agitter.domain.contacts;

import static infra.util.Collections.copy;
import static infra.util.ToString.sortIgnoreCase;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;
import utils.XssAttackSanitizer;

public class GroupImpl2 implements Group {
	
	private String name;
	private List<User> members = new ArrayList<User>();
	private List<Group> subgroups = new ArrayList<Group>();


	public GroupImpl2(String groupName) throws Refusal {
		setName(groupName);
	}

	
	@Override
	public String toString() {
		return name();
	}


	@Override
	public String name() {
		return name;
	}


	void setName(String newName) throws Refusal {
		validateGroupName(newName);
		name = newName;
	}


	@Override
	public List<User> immediateMembers() {
		return copy(members);
	}
	
	
	void addMember(User member) {
		if (members.contains(member)) return;
		members.add(member);
		sortIgnoreCase(members);
	}
	
	
	void removeMember(User member) {
		members.remove(member);
	}


	@Override
	public List<Group> immediateSubgroups() {
		return copy(subgroups);
	}
	
	
	@Override
	public void addSubgroup(Group subgroup) {
		if (subgroup == this) return;
		if (subgroups.contains(subgroup)) return;
		subgroups.add(subgroup);
		sortIgnoreCase(subgroups);
	}


	@Override
	public void removeSubgroup(Group subgroup) {
		subgroups.remove(subgroup);
	}


	@Override
	public boolean deepContains(User user) {
		ArrayList<Group> visited = new ArrayList<Group>();
		return deepContains(user, visited);
	}

	
	private boolean deepContains(User user, ArrayList<Group> visited) {
		if (visited.contains(this)) return false;
		visited.add(this);
		if (members.contains(user)) return true;
		for (Group subgroup : subgroups)
			if (((GroupImpl2)subgroup).deepContains(user, visited))
				return true;
		return false;
	}


	private void validateGroupName(String groupName) throws Refusal {
		if( groupName == null || groupName.trim().isEmpty() )
			throw new Refusal("O nome do grupo deve ser preenchido.");
		if(!XssAttackSanitizer.ultraConservativeFilter(groupName).equals(groupName) || groupName.contains("@"))
			throw new Refusal("O nome do grupo deve possuir somente letras e números.");
	}
	
}
