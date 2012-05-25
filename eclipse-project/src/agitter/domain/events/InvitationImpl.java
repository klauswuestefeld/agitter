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
	private Set<InvitationImpl> invitees = new HashSet<InvitationImpl>();
	private Set<Group> groupInvitees = new HashSet<Group>();
	
	public InvitationImpl(User from) {
		if (from == null) throw new IllegalArgumentException("Host cannot be null");
		this.host = from;
	}
	
	@Override
	public User host() {
		return host;
	}
	
	@Override
	public Invitation[] directInvitees() {
		return actualInvitees().toArray(new Invitation[actualInvitees().size()]);
	}
	
	@Override
	public Group[] directGroupInvitees() {
		return actualGroupInvitees().toArray(new Group[actualGroupInvitees().size()]);
	}
	
	private Set<InvitationImpl> actualInvitees() {
		if (invitees==null) invitees = new HashSet<InvitationImpl>();
		return invitees;
	}
	
	private Set<Group> actualGroupInvitees() {
		if (groupInvitees==null) groupInvitees = new HashSet<Group>();
		return groupInvitees;
	}


	boolean isInvited(User user) {
		return userThatInvited(user) != null;
	}
	
	private boolean isInvited(Group invitee) {
		return isInvitedBy(invitee) != null;
	}
	
	User userThatInvited(User user) {
		// If it is into a group invited by this host.
		for (Group g : actualGroupInvitees())
			if (g.deepContains(user)) 
				return host;
		
		// Search in the invitations. 
		for (InvitationImpl i : actualInvitees()) {
			if (i.host().equals(user)) return host; 
			
			// Recursion.
			User h = i.userThatInvited(user);
			if (h != null) 
				return h;
		}
		
		return null;
	}
	
	private User isInvitedBy(Group invitee) {
		// Search in the invitations. 
		for (InvitationImpl i : actualInvitees()) {
			// Recursion.
			User h = i.isInvitedBy(invitee);
			if (h != null) 
				return h;
		}
		
		return null;
	}
	
	private void addInvitee(User invitee) {
		actualInvitees().add(new InvitationImpl(invitee));
	}
	
	private void addInvitee(Group invitee) {
		actualGroupInvitees().add(invitee);
	}

	boolean removeInvitee(User invitee) {
		for (InvitationImpl i : actualInvitees()) { 
			if (i.host().equals(invitee)) {
				actualInvitees().remove(i);
				return true;
			} 
			
			if (i.removeInvitee(invitee)) 
				return true;
		}
		
		return false;
	}
	
	boolean removeInvitee(Group invitee) {
		if (actualGroupInvitees().contains(invitee)) {
			actualGroupInvitees().remove(invitee);
			return true;
		}
		
		for (InvitationImpl i : actualInvitees()) { 
			if (i.removeInvitee(invitee)) 
				return true;
		}
		
		return false;
	}
	
	Set<User> allResultingInvitees() {
		Set<User> result = new HashSet<User>();
				
		for (Group g : actualGroupInvitees()) {
			result.addAll(g.deepMembers());
		}
		
		for (InvitationImpl i : actualInvitees()) {
			result.add(i.host());
			result.addAll(i.allResultingInvitees());
		}
		
		return result;
	}
	
	void transferHostTo(User newhost) {
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
	

	boolean invite(User host, User newInvitee) {
		if (this.host == newInvitee) return true;
		if (isInvited(newInvitee)) return true;
		
		if (recursiveInvite(host, newInvitee)) return true;
		
		if (createAnInvitationForAUserIntoAGroup(host))
			return recursiveInvite(host, newInvitee);
			
		return false;
	}
	
	
	private boolean createAnInvitationForAUserIntoAGroup(User invitee) {
		for (Group g : actualGroupInvitees()) {
			if (g.deepContains(invitee)) {
				addInvitee(invitee);
				return true;
			}
		}
		
		for (InvitationImpl i : actualInvitees()) {
			if (i.createAnInvitationForAUserIntoAGroup(invitee)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean recursiveInvite(User host, User newInvitee) {
		if (isTheHost(host)) {
			addInvitee(newInvitee);
			return true;
		}
			
		// First go deep to find the host. 
		for (InvitationImpl i : actualInvitees()) {
			if (i.recursiveInvite(host, newInvitee)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	boolean invite(User host, Group newInvitee) {
		if (isInvited(newInvitee)) return true;
		
		if (recursiveInvite(host, newInvitee)) return true;
		
		// Could not invite
		// The host should be into a Group. 
		// Create an invitation for the host
		if (createAnInvitationForAUserIntoAGroup(host))
			return recursiveInvite(host, newInvitee);
			
		return false;
	}
	
	public boolean recursiveInvite(User host, Group newInvitee) {
		if (isTheHost(host)) {
			addInvitee(newInvitee);
			return true;
		}
		 
		for (InvitationImpl i : actualInvitees()) {
			if (i.recursiveInvite(host, newInvitee)) {
				return true;
			}
		}
		
		return false;
	}
}
