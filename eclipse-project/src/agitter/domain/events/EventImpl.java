package agitter.domain.events;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class EventImpl implements Event {
	
	final private String _description;
	final private long _datetime;
	final private User _owner;
	
	private Set<Group> groupInvitations = new HashSet<Group>();
	private Set<User> invitees = new HashSet<User>();
	
	final private Set<User> notInterested = new HashSet<User>();
	
	
	public EventImpl(User owner, String description, long datetime, List<Group> inviteeGroups, List<User> invitees) throws Refusal {
		if(null==owner) { throw new IllegalArgumentException("user cannot be null"); }
		if(datetime==0L) { throw new Refusal("Data do agito deve ser preenchida."); }
		if(null==description) { throw new Refusal("Descrição do agito deve ser preenchida."); }
		_owner = owner;
		_description = description;
		_datetime = datetime;
		groupInvitees().addAll(inviteeGroups);
		invitees().addAll(invitees);
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
	public void addInvitees(Group group) {
		groupInvitees().add(group);
	}


	@Override
	public void addInvitee(User user) {
		invitees().add(user);
	}


	@Override
	public void notInterested(User user) {
		if(owner().equals(user)) throw new IllegalArgumentException( "Dono do agito deve estar interessado no agito." );
		
		notInterested.add(user);
	}
	
	
	private boolean isInterested(User user) {
		return !notInterested.contains(user);
	}
	
	
	boolean isVisibleTo(User user) {
		if (owner().equals(user)) return true;
		return isInvited(user) && isInterested(user);
	}


	private boolean isInvited(User user) {
		return invitees().contains(user) || groupInvitationsContain(user);
	}


	private boolean groupInvitationsContain(User user) {
		for (Group group : groupInvitees())
			if (group.deepContains(user))
				return true;
		return false;
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

	
	synchronized
	private Set<User> invitees() {
		if (invitees==null) invitees = new HashSet<User>();
		return invitees;
	}

	
	synchronized
	private Set<Group> groupInvitees() {
		if (groupInvitations==null) groupInvitations = new HashSet<Group>();
		return groupInvitations;
	}
	
}
