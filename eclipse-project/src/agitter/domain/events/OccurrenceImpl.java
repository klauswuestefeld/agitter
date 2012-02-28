package agitter.domain.events;

import java.util.HashSet;
import java.util.Set;

import org.prevayler.bubble.PrevalentBubble;

import agitter.domain.users.User;

public class OccurrenceImpl implements Occurrence {

	{
		PrevalentBubble.idMap().register(this);
	}
	
	private Set<User> notInterested = new HashSet<User>();
	private long datetime;
	
	public OccurrenceImpl(long datetime) {
		this.datetime = datetime;
	}
	
	@Override
	public long datetime() {
		return datetime;
	}

	@Override
	public void notInterested(User user) {	
		notInterested().add(user);
	}
	
	@Override
	public boolean isInterested(User user) {
		return !notInterested().contains(user);
	}
	
	public Set<User> notInterested() {
		if (notInterested == null) { 
			notInterested = new HashSet<User>();
		}
		return notInterested;
	}
}
