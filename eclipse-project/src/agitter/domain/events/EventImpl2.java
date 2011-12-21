package agitter.domain.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class EventImpl2 implements Event {
	
	private static final long ONE_HOUR = 1000 * 60 * 60;

	@SuppressWarnings("unused")	@Deprecated transient private long id; //2011-10-19

	final private User _owner;
	private String _description;
	private long _datetime;
	
	private Set<Group> groupInvitations = new HashSet<Group>();
	private Set<User> invitees = new HashSet<User>();
	
	final private Set<User> notInterested = new HashSet<User>();
	
	
	public EventImpl2(User owner, String description, long datetime) throws Refusal {
		if(null==owner) { throw new IllegalArgumentException("user cannot be null"); }
		_owner = owner;
		edit(description, datetime);
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
	synchronized
	public User[] invitees() {
		return actualInvitees().toArray(new User[actualInvitees().size()]);
	}


	private Set<User> actualInvitees() {
		if (invitees==null) invitees = new HashSet<User>();
		return invitees;
	}

	
	@Override
	synchronized
	public Group[] groupInvitees() {
		return actualGroupInvitees().toArray(new Group[actualGroupInvitees().size()]);
	}


	private Set<Group> actualGroupInvitees() {
		if (groupInvitations==null) groupInvitations = new HashSet<Group>();
		return groupInvitations;
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
		return actualInvitees().contains(user) || groupInvitationsContain(user);
	}


	private boolean groupInvitationsContain(User user) {
		for (Group group : actualGroupInvitees())
			if (group.deepContains(user))
				return true;
		return false;
	}


	private void assertIsInTheFuture(long datetime) throws Refusal {
		if (datetime < Clock.currentTimeMillis() - ONE_HOUR)
			throw new Refusal("Agito no passado??!?");
	}


	void edit(String newDescription, long newDatetime) throws Refusal {
		if (null == newDescription) { throw new Refusal("Descrição do agito deve ser preenchida."); }
		assertIsInTheFuture(newDatetime);

		_description = newDescription;
		_datetime = newDatetime;
	}


	@Override
	public void addInvitee(User invitee) {
		actualInvitees().add(invitee);
	}


	@Override
	public void removeInvitee(User invitee) {
		actualInvitees().remove(invitee);
	}


	@Override
	public void addInvitee(Group invitee) {
		actualGroupInvitees().add(invitee);
	}


	@Override
	public void removeInvitee(Group invitee) {
		actualGroupInvitees().remove(invitee);
	}


	@Override
	public List<User> allResultingInvitees() {
		Set<User> result = new HashSet<User>(invitees);
		for(Group g : groupInvitations)
			result.addAll(g.deepMembers());
		return new ArrayList<User>(result);
	}

}
