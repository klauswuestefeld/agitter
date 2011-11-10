package agitter.domain.events;

import java.util.Set;

import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

@Deprecated
@SuppressWarnings("unused")
public class EventImpl implements Event {
	String _description;
	long _datetime;
	User _owner;
	
	private Set<Group> groupInvitations;
	private Set<EmailAddress> emailInvitations;
	
	private Set<User> notInterested;

	
	@Override
	public String description() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public long datetime() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public User[] invitees() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	public long id() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	@Override
	public User owner() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void notInterested(User user) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Group[] groupInvitees() {
		return new Group[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

}
