
package agitter.domain.contacts;

import java.util.List;

import agitter.domain.emails.EmailAddress;

public interface Group {

	String name();
	
	List<EmailAddress> immediateContacts();
	List<Group> immediateSubgroups();
	void addSubgroup(Group subgroup);
	void removeSubgroup(Group subgroup);

	boolean deepContains(EmailAddress mail);

}
