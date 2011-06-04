package agitter.domain.contacts;

import sneer.foundation.lang.CacheMap;
import sneer.foundation.lang.Producer;
import agitter.domain.users.User;

public class ContactsImpl implements Contacts {
	
	private static final Producer<Group> NEW_ROOT_GROUP = new Producer<Group>() { @Override public Group produce() {
		return new GroupImpl( "Todos" );
	}};

	private final CacheMap<User, Group> rootGroupsByUser = CacheMap.newInstance();

	@Override
	public Group allContactsFor(User user) {
		return rootGroupsByUser.get(user, NEW_ROOT_GROUP);
	}

}
