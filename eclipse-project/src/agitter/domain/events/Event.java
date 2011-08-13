package agitter.domain.events;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {
	String description();
	long datetime();
	User owner();

	void addInvitees(Group group);
	void addInvitee(User user);

	void notInterested(User user);
}
