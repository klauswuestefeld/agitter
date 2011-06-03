
package agitter.domain.contacts;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;

import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;


public interface Contacts {

	List<EmailAddress> contactsFor(User user);

	void addContactFor(User user, EmailAddress emailAddress);

	List<Group> groupsFor(User user);

	@Transaction
	Group addGroupFor(User user, String string) throws Refusal;

}
