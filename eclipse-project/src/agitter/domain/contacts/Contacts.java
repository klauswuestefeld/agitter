
package agitter.domain.contacts;

import agitter.domain.users.User;


public interface Contacts {

	ContactsOfAUser contactsOf(User user);

}
