
package agitter.domain.contacts;

import java.util.List;

import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;


public interface Contacts {

	List<EmailAddress> contactsFor(User user);

	void addFor(User user, EmailAddress emailAddress);

}
