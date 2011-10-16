package agitter.domain.events;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class EventImpl2 implements Event {
	
	final private User _owner;
	private String _description;
	private long _datetime;
	
	private Set<Group> groupInvitations = new HashSet<Group>();
	private Set<User> invitees = new HashSet<User>();
	
	final private Set<User> notInterested = new HashSet<User>();
	
	
	public EventImpl2(User owner, String description, long datetime, List<Group> inviteeGroups, List<User> invitees) throws Refusal {
		if(null==owner) { throw new IllegalArgumentException("user cannot be null"); }
		if(datetime==0L) { throw new Refusal("Data do agito deve ser preenchida."); }
		if(null==description) { throw new Refusal("Descrição do agito deve ser preenchida."); }
		_owner = owner;
		setDescription(description);
		setDatetime(datetime);
		groupInvitees().addAll(inviteeGroups);
		invitees().addAll(invitees);
	}

	
	@Override
	public User owner() {
		return _owner;
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
	public void setDescription(String newDescription) {
		_description = newDescription;
	}


	@Override
	public void setDatetime(long newDatetime) throws Refusal {
		assertIsInTheFuture(newDatetime);
		_datetime = newDatetime;
	}


	@Override public void addInvitee(User user) { invitees().add(user); }
	@Override public void addInvitees(Group group) { groupInvitees().add(group); }
	@Override public void removeInvitee(User user) {  invitees().remove(user); }
	@Override public void removeInvitees(Group group) { groupInvitees().remove(group); }
	

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

	
	private void assertIsInTheFuture(long datetime) throws Refusal {
		if (datetime < Clock.currentTimeMillis())
			throw new Refusal("Novos eventos devem ser criados com data futura.");
	}

}
