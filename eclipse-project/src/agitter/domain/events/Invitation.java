package agitter.domain.events;

import java.util.Set;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Invitation {
	User host(); // owner
	Invitation[] invitees();
	Group[] groupInvitees();
	
	void transferHostTo(User newhost); // only when accounts are merged
	
	boolean removeInvitee(User invitee);
	boolean removeInvitee(Group invitee);
	
	boolean invite(User host, User newInvitee);
	boolean invite(User host, Group newInvitee);
	
	Set<User> allResultingInvitees();
	
	boolean isInvited(User invitee);
	User userThatInvited(User invitee);
	
	boolean isInvited(Group invitee);
	User isInvitedBy(Group invitee);
}