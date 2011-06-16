package agitter.domain.contacts;

import sneer.foundation.lang.CacheMap;
import sneer.foundation.lang.Producer;
import agitter.domain.users.User;

public class ContactsImpl implements Contacts {
	
	@SuppressWarnings("unused")
	@Deprecated
	transient private CacheMap<User, Group> rootGroupsByUser;
	
	private static final Producer<ContactsOfAUser> NEW_CONTACTS_OF_A_USER = new Producer<ContactsOfAUser>() { @Override public ContactsOfAUser produce() {
		return new ContactsOfAUserImpl();
	}};

	private CacheMap<User, ContactsOfAUser> contactsByUser = CacheMap.newInstance();

	
	@Override
	public ContactsOfAUser contactsOf(User user) {
		return contactsByUser().get(user, NEW_CONTACTS_OF_A_USER);
	}


	private CacheMap<User, ContactsOfAUser> contactsByUser() {
		if (contactsByUser == null) contactsByUser = CacheMap.newInstance(); //Redundant because of schema evolution.
		return contactsByUser;
	}

}
