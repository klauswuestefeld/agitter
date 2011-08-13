package agitter.domain.contacts;

import sneer.foundation.lang.CacheMap;
import agitter.domain.users.User;

@Deprecated
public class ContactsImpl implements Contacts {
	
	@SuppressWarnings("unused")
	@Deprecated
	transient private CacheMap<User, Group> rootGroupsByUser;
	
	CacheMap<User, ContactsOfAUser> contactsByUser = CacheMap.newInstance();

	@Override
	public ContactsOfAUser contactsOf(User user) {
		throw new IllegalStateException("Deprecated");
	}

}
