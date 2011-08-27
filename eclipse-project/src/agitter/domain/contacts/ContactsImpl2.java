package agitter.domain.contacts;

import sneer.foundation.lang.CacheMap;
import sneer.foundation.lang.Producer;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.Users;

public class ContactsImpl2 implements Contacts {
	
	private static final Producer<ContactsOfAUser> NEW_CONTACTS_OF_A_USER = new Producer<ContactsOfAUser>() { @Override public ContactsOfAUser produce() {
		return new ContactsOfAUserImpl2();
	}};

	public ContactsImpl2() {}
	
	@SuppressWarnings("deprecation")
	public ContactsImpl2(ContactsImpl old, Users users) {
		for (User user : old.contactsByUser.keySet()) {
			ContactsOfAUserImpl oldContactsOfAUser = (ContactsOfAUserImpl) old.contactsByUser.get(user);
			ContactsOfAUser newContactsOfAUser = contactsOf(user);
			if (oldContactsOfAUser == null || oldContactsOfAUser.all == null) {
				System.out.println("User without contacts.");
				continue;
			}
			for (EmailAddress oldContact : oldContactsOfAUser.all)
				newContactsOfAUser.addContact(user(oldContact, users));
		}
	}

	private CacheMap<User, ContactsOfAUser> contactsByUser = CacheMap.newInstance();

	
	@Override
	public ContactsOfAUser contactsOf(User user) {
		return contactsByUser.get(user, NEW_CONTACTS_OF_A_USER);
	}


	private User user(EmailAddress oldContact, Users users) {
		return users.produce(oldContact);
	}
}

