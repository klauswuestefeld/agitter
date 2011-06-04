
package agitter.domain.contacts;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.EmailAddress;

public interface Group extends Comparable<Group> {

	String name();
	
	List<EmailAddress> contacts();
	void addContact(EmailAddress emailAddress);
	
	List<Group> subgroups();
	@Transaction
	Group addSubgroup(String name) throws Refusal;

}
