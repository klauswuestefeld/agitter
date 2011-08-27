package agitter.domain.events;

import java.util.HashSet;
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
	
	private Set<Group> groupInvitations = new HashSet<Group>();
	private Set<EmailAddress> emailInvitations = new HashSet<EmailAddress>();
	
	final private Set<User> notInterested = new HashSet<User>();

	
	@Override
	public String description() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public long datetime() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public User owner() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void addInvitees(Group group) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void addInvitee(User user) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void notInterested(User user) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
}
