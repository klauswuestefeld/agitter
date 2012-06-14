package agitter.domain.contacts;

import basis.lang.CacheMap;
import basis.lang.Producer;
import basis.lang.exceptions.Refusal;
import agitter.domain.users.User;

public class ContactsImpl2 implements Contacts {
	
	private static final Producer<ContactsOfAUser> NEW_CONTACTS_OF_A_USER = new Producer<ContactsOfAUser>() { @Override public ContactsOfAUser produce() {
		return new ContactsOfAUserImpl2();
	}};

	private CacheMap<User, ContactsOfAUser> contactsByUser = CacheMap.newInstance();

	
	@Override
	public ContactsOfAUser contactsOf(User user) {
		return contactsByUser.get(user, NEW_CONTACTS_OF_A_USER);
	}


	@Override
	public void transferContacts(User takingCareOf, User beingDropped) throws Refusal {
		contactsOf(takingCareOf).addAll(contactsOf(beingDropped));
	}

}

