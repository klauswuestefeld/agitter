package agitter.domain.contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import sneer.foundation.lang.CacheMap;
import sneer.foundation.lang.Producer;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

public class ContactsImpl implements Contacts {
	
	private static final Producer<SortedSet<EmailAddress>> NEW_EMPTY_LIST = new Producer<SortedSet<EmailAddress>>() { @Override public SortedSet<EmailAddress> produce() {
		return new TreeSet<EmailAddress>();
	}};

	private final CacheMap<User, SortedSet<EmailAddress>> contactsByUser = CacheMap.newInstance();
	
	@Override
	public List<EmailAddress> contactsFor(User user) {
		return new ArrayList<EmailAddress>( contactsSetFor(user) );
	}

	@Override
	public void addFor(User user, EmailAddress emailAddress) {
		contactsSetFor(user).add(emailAddress);
	}
	
	private SortedSet<EmailAddress> contactsSetFor(User user) {
		return contactsByUser.get(user, NEW_EMPTY_LIST);
	}
	

}
