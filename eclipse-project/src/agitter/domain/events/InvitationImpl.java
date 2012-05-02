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
	public Invitation[] invitees() {
		return actualInvitees().toArray(new Invitation[actualInvitees().size()]);
	}
	
	@Override
	public Group[] groupInvitees() {
		return actualGroupInvitees().toArray(new Group[actualGroupInvitees().size()]);
	}
	
	private Set<Invitation> actualInvitees() {
		if (invitees==null) invitees = new HashSet<Invitation>();
		return invitees;
	}
	
	private Set<Group> actualGroupInvitees() {
		if (groupInvitees==null) groupInvitees = new HashSet<Group>();
		return groupInvitees;
	}


	@Override
	public boolean isInvited(User user) {
		return isInvitedBy(user) != null;
	}
	
	@Override
	public boolean isInvited(Group invitee) {
		return isInvitedBy(invitee) != null;
	}
	
	@Override
	public User isInvitedBy(User user) {
		// If it is into a group invited by this host.
		for (Group g : actualGroupInvitees()) {
			if (g.deepContains(user)) 
				return host();
		}
		
		// Search in the invitations. 
		for (Invitation i : actualInvitees()) {
			if (i.host().equals(user)) return host(); 
			
			// Recursion.
			User h = i.isInvitedBy(user);
			if (h != null) 
				return h;
		}
		
		return null;
	}
	
	@Override
	public User isInvitedBy(Group invitee) {
		// If it is into a group invited by this host.
		if (actualGroupInvitees().contains(invitee)) {
			host();
		}
	
		// Search in the invitations. 
		for (Invitation i : actualInvitees()) {
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

	@Override
	public boolean removeInvitee(User invitee) {
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
	public boolean removeInvitee(Group invitee) {
		if (actualGroupInvitees().contains(invitee)) {
			actualGroupInvitees().remove(invitee);
			return true;
		}
		
		for (Invitation i : actualInvitees()) { 
			if (i.removeInvitee(invitee)) 
				return true;
		}
		
		return false;
	}
	
	@Override
	public Set<User> allResultingInvitees() {
		Set<User> result = new HashSet<User>();
				
		for (Group g : actualGroupInvitees()) {
			result.addAll(g.deepMembers());
		}
		
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
	
	// **************************************************************
	// Add an invitee: 
	// 0. Do not insert if it is already invited.
	// 1. Look for the Invitation owned by the host. 
	// 2. Add the invitee. 
	// 3. If there is no Invitation owned by the host, the host may be into a group. 
	// 4. Create a proper invitation for the host. 
	// 5. Add the invitee to the host invitation. 
	// 6. User will not be removed if the group is removed. 
	// **************************************************************
	@Override
	public boolean invite(User host, User newInvitee) {
		if (host() == newInvitee) return true;
		if (isInvited(newInvitee)) return true;
		
		if (recursiveInvite(host, newInvitee)) return true;
		
		// Could not invite
		// The host should be into a Group. 
		// Create an invitation for the host
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
		
		for (Invitation i : actualInvitees()) {
			if (((InvitationImpl)i).createAnInvitationForAUserIntoAGroup(invitee)) {
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
		for (Invitation i : actualInvitees()) {
			if (((InvitationImpl)i).recursiveInvite(host, newInvitee)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public boolean invite(User host, Group newInvitee) {
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
		 
		for (Invitation i : actualInvitees()) {
			if (((InvitationImpl)i).recursiveInvite(host, newInvitee)) {
				return true;
			}
		}
		
		return false;
	}
}
