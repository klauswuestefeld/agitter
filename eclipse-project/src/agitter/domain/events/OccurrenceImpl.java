package agitter.domain.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.prevayler.bubble.PrevalentBubble;

import agitter.domain.users.User;

public class OccurrenceImpl implements Occurrence {

	{
		PrevalentBubble.idMap().register(this);
	}
	
	private Set<User> notInterested = new HashSet<User>();
	private long datetime;
	private Map<User, Boolean> attendanceStatus = new HashMap<User, Boolean>();
	//private Set<Decision> attendanceStatus = new HashSet<Decision>();
	
	public OccurrenceImpl(long datetime, User owner) {
		this.datetime = datetime;
		going(owner);
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
	
	private Map<User,Boolean> attendanceStatus() {
		if (attendanceStatus == null) { 
			attendanceStatus = new HashMap<User, Boolean>();
		}
		return attendanceStatus;
	}

	@Override
	public void going(User user) {
		attendanceStatus().put(user, true);
	}

	@Override
	public void notGoing(User user) {
		attendanceStatus().put(user, false);
	}

	@Override
	public void mayGo(User user) {
		attendanceStatus().put(user, null);
	}

	@Override
	public boolean hasIgnored(User user) {
		return !notInterested.contains(user) && !attendanceStatus().containsKey(user);
	}
	
	@Override
	public Boolean isGoing(User user) {	
		return attendanceStatus().get(user);
	}

	@Override
	public void copyBehavior(User leader, User sheep) {
		if (!isInterested(leader)) 
			notInterested(sheep);
		
		if (isGoing(leader) != null) {
			if (isGoing(leader)) 	
				going(sheep);
			else 
				notGoing(sheep);
		}
	}
}
