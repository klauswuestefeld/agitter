package agitter.domain.events;


import java.io.Serializable;
import java.util.HashSet;

import agitter.domain.User;
import sneer.foundation.lang.ReadOnly;

public class PublicEvent implements Serializable, ReadOnly {

	private static final long serialVersionUID = 1L;

	final private User _owner;
	final private long _datetime;
	final private String _description;

	final private HashSet<User> notInterested = new HashSet<User>();

	public PublicEvent(User owner, String description, long datetime) {
		if(null==owner) { throw new IllegalArgumentException("user can not be null"); }
		if(datetime==0) { throw new IllegalArgumentException("Data do agito deve ser preenchida."); }
		if(null==description) { throw new IllegalArgumentException("description can not be null"); }
		_owner = owner;
		_description = description;
		_datetime = datetime;
	}

	public String description() {
		return _description;
	}

	public long datetime() {
		return _datetime;
	}

	public HashSet<User> getNotInterested() {
		return notInterested;
	}
	public void notInterested(User user) {
		this.notInterested.add(user);
	}

	@Override
	public boolean equals(Object o) {
		if(this==o) { return true; }
		if(o==null || getClass()!=o.getClass()) { return false; }

		PublicEvent agito = (PublicEvent) o;

		return _datetime==agito._datetime && _description.equals(agito._description);
	}

	@Override
	public int hashCode() {
		int result = (int) _datetime;
		result = 31*result+_description.hashCode();
		return result;
	}

}
