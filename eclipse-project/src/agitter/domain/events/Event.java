package agitter.domain.events;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Event {
	String description();
	void setDescription(String newDescription);
	long datetime();
	void setDatetime(long newDatetime) throws Refusal;
	User owner();

	void addInvitees(Group group);
	void removeInvitees(Group group);
	void addInvitee(User user);
	void removeInvitee(User user);

	void notInterested(User user);
}
