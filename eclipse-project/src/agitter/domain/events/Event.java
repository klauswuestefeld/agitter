package agitter.domain.events;

import java.util.List;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	User owner();

	String description();
	long[] datetimes();

	User[] invitees();
	void addInvitee(User invitee);
	void removeInvitee(User invitee);
	Group[] groupInvitees();
	void addInvitee(Group invitee);
	void removeInvitee(Group invitee);

	void notInterested(User user);

	List<User> allResultingInvitees();
	
}
