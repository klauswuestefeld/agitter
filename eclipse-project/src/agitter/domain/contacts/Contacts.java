
package agitter.domain.contacts;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;


public interface Contacts {

	ContactsOfAUser contactsOf(User user);

	void transferContacts(User takingCareOf, User beingDropped) throws Refusal;

}
