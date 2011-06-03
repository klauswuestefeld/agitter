
package agitter.domain.contacts;

import java.util.List;

import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;


public interface Contacts {

	List<EmailAddress> contactsFor(User user);

	void addContactFor(User user, EmailAddress emailAddress);

	List<Group> groupsFor(User user);

	void addGroupFor(User user, String string);

}
