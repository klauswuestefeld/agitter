
package agitter.domain.contacts;

import agitter.domain.users.User;


public interface Contacts {

	Group allContactsFor(User user);

}