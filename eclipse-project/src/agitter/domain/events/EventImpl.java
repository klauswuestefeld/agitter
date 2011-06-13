package agitter.domain.events;


import java.util.HashSet;
import java.util.Set;

import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

public class EventImpl implements Event {

	final private String _description;
	final private long _datetime;
	final private User _owner;

	final private Set<User> notInterested = new HashSet<User>();

	
	public EventImpl(User owner, String description, long datetime) {
		if(null==owner) { throw new IllegalArgumentException("user can not be null"); }
		if(datetime==0L) { throw new IllegalArgumentException("Data do agito deve ser preenchida."); }
		if(null==description) { throw new IllegalArgumentException("description can not be null"); }
		_owner = owner;
		_description = description;
		_datetime = datetime;
	}

	
	@Override
	public String description() {
		return _description;
	}

	
	@Override
	public long datetime() {
		return _datetime;
	}

	
	@Override
	public User owner() {
		return _owner;
	}

	
	@Override
	public void notInterested(User user) {
		notInterested.add(user);
	}

	
	@Override
	public boolean equals(Object o) {
		if(this==o) { return true; }
		if(o==null || getClass()!=o.getClass()) { return false; }

		EventImpl agito = (EventImpl) o;

		return _datetime==agito._datetime && _description.equals(agito._description);
	}

	
	@Override
	public int hashCode() {
		int result = (int) _datetime;
		result = 31*result+_description.hashCode();
		return result;
	}

	
	boolean isInterested(User user) {
		return !notInterested.contains(user);
	}


	@Override
	public void addInvitation(EmailAddress emailAddress) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

}
