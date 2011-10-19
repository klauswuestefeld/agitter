package agitter.domain.events;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {

	long id();

	User owner();

	String description();
	void setDescription(String newDescription);
	long datetime();

	void addInvitees(Group group);
	void removeInvitees(Group group);
	void addInvitee(User user);
	void removeInvitee(User user);

	void notInterested(User user);
}
