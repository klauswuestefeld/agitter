package agitter.domain.contacts;

import sneer.foundation.lang.CacheMap;
import sneer.foundation.lang.Producer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;

public class ContactsImpl implements Contacts {
	
	private static final Producer<Group> NEW_ROOT_GROUP = new Producer<Group>() { @Override public Group produce() {
		try {
			return new GroupImpl( "Todos" );
		} catch (Refusal e) {
			throw new IllegalStateException( e );
		}
	}};

	private final CacheMap<User, Group> rootGroupsByUser = CacheMap.newInstance();

	@Override
	public Group allContactsFor(User user) {
		return rootGroupsByUser.get(user, NEW_ROOT_GROUP);
	}

}
