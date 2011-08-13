
package agitter.domain.contacts;

import java.util.List;

import agitter.domain.users.User;

public interface Group {

	String name();
	
	List<User> immediateMembers();
	List<Group> immediateSubgroups();
	void addSubgroup(Group subgroup);
	void removeSubgroup(Group subgroup);

	boolean deepContains(User user);

}
