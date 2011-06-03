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
	
	private static final Producer<SortedSet<EmailAddress>> NEW_EMAIL_SET = new Producer<SortedSet<EmailAddress>>() { @Override public SortedSet<EmailAddress> produce() {
		return new TreeSet<EmailAddress>();
	}};
	
	private static final Producer<SortedSet<Group>> NEW_GROUP_SET = new Producer<SortedSet<Group>>() { @Override public SortedSet<Group> produce() {
		return new TreeSet<Group>();
	}};

	private final CacheMap<User, SortedSet<EmailAddress>> contactsByUser = CacheMap.newInstance();
	
	private final CacheMap<User, SortedSet<Group>> groupsByUser = CacheMap.newInstance();
	
	@Override
	public List<EmailAddress> contactsFor(User user) {
		return new ArrayList<EmailAddress>( contactSetFor(user) );
	}

	@Override
	public void addContactFor(User user, EmailAddress emailAddress) {
		contactSetFor(user).add(emailAddress);
	}
	
	@Override
	public void addGroupFor(User user, String groupName) {
		groupSetFor(user).add( new GroupImpl( groupName ) );
	}
	
	@Override
	public List<Group> groupsFor(User user) {
		return new ArrayList<Group>( groupSetFor(user) );
	}
	
	private SortedSet<EmailAddress> contactSetFor(User user) {
		return contactsByUser.get(user, NEW_EMAIL_SET);
	}
	
	private SortedSet<Group> groupSetFor(User user) {
		return groupsByUser.get(user, NEW_GROUP_SET);
	}

}
