package agitter.domain.events;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	User owner();

	String description();
	long datetime();

	User[] invitees();
	void addInvitee(User user);
	void removeInvitee(User user);
	Group[] groupInvitees();
	void addInvitees(Group group);
	void removeInvitees(Group group);

	void notInterested(User user);


}
