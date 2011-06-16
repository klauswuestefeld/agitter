
package agitter.domain.contacts;

import java.util.List;

import agitter.domain.emails.EmailAddress;

public interface Group {

	String name();
	
	List<EmailAddress> immediateMembers();
	List<Group> immediateSubgroups();
	void addSubgroup(Group subgroup);
	void removeSubgroup(Group subgroup);

	boolean deepContains(EmailAddress mail);

}
