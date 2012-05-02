package agitter.domain.events;

import agitter.domain.users.User;

public class InvitedActions {
	Event invitedTo;
	User invited;
	
	public InvitedActions(Event invitedTo, User userInvited) {
		this.invitedTo = invitedTo; 
		this.invited = userInvited;
	}
	
	void invites(User invitee) {
		invitedTo.invite(invitee, invited);
	}
	
	void blocks() {
		invitedTo.notInterested(invited);
	}
	
	void blocks(long datetime) {
		invitedTo.notInterested(invited, datetime);
	}
	
	void accepts(long datetime) {
		invitedTo.going(invited, datetime);
	}
	
	void declines(long datetime) {
		invitedTo.notGoing(invited, datetime);
	}
	
	void couldNotDecideFor(long datetime) {
		invitedTo.mayGo(invited, datetime);
	}
}
