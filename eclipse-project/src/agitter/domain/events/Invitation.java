package agitter.domain.events;

import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Invitation {
	User host(); // owner

	Invitation[] directInvitees();
	Group[] directGroupInvitees();

}