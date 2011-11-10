package agitter.domain.events;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	User owner();

	String description();
	long datetime();

	User[] invitees();
	Group[] groupInvitees();

	void notInterested(User user);


}
