package agitter.domain.events;

import java.util.HashSet;
import java.util.Set;

import org.prevayler.bubble.PrevalentBubble;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class InvitationImpl implements Invitation {

	{
		PrevalentBubble.idMap().register(this);
	}
	
	private User host;
	private Set<Invitation> invitees = new HashSet<Invitation>();
	
	public InvitationImpl(User from) {
		if (from == null) throw new IllegalArgumentException("Host cannot be null");
		this.host = from;
	}
	
	@Override
	public User host() {
		return host;
	}
	
	@Override
	public Invitation[] invitees() {
		return actualInvitees().toArray(new Invitation[actualInvitees().size()]);
	}
	
	private Set<Invitation> actualInvitees() {
		if (invitees==null) invitees = new HashSet<Invitation>();
		return invitees;
	}

	@Override
	public boolean isInvited(User user) {
		return isInvitedBy(user) != null;
	}
	
	@Override
	public User isInvitedBy(User user) {
		for (Invitation i : actualInvitees()) {
			if (i.host().equals(user)) return host(); 
			
			User h = i.isInvitedBy(user);
			if (h != null) 
				return h;
		}
		
		return null;
	}
	
	private void addInvitee(User invitee) {
		actualInvitees().add(new InvitationImpl(invitee));
	}
	
	private void addInvitee(Group invitee) {
		for (User u : invitee.deepMembers())
			addInvitee(u);
	}

	@Override
	public boolean removeInvitee(User invitee) {
		// TODO: What happens when an invitee which has invites is deleted?
		for (Invitation i : actualInvitees()) { 
			if (i.host().equals(invitee)) {
				actualInvitees().remove(i);
				return true;
			} 
			
			if (i.removeInvitee(invitee)) 
				return true;
		}
		
		return false;
	}
	
	@Override
	public Set<User> allResultingInvitees() {
		Set<User> result = new HashSet<User>();
				
		for (Invitation i : actualInvitees()) {
			result.add(i.host());
			result.addAll(i.allResultingInvitees());
		}
		
		return result;
	}
	
	@Override
	public void transferHostTo(User newhost) {
		this.host = newhost;
	}

	public void inviteAllUsers(User host, Set<User> invitees) {
		for (User u : invitees) 
			invite(host, u);
	}
	
	public void inviteAllGroups(User host, Set<Group> invitees) {
		for (Group g : invitees)
			invite(host, g);
	}

	private boolean isTheHost(User user) {
		return host().equals(user);
	}
	
	@Override
	public boolean invite(User host, User newInvitee) {
		if (isTheHost(host)) {
			addInvitee(newInvitee);
			return true;
		}
		 
		for (Invitation i : actualInvitees()) {
			if (i.invite(host, newInvitee)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public boolean invite(User host, Group newInvitee) {
		if (isTheHost(host)) {
			addInvitee(newInvitee);
			return true;
		}
		 
		for (Invitation i : actualInvitees()) {
			if (i.invite(host, newInvitee)) {
				return true;
			}
		}
		
		return false;
	}
}
