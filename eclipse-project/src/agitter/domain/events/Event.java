package agitter.domain.events;

import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;

public interface Event {
	String description();
	long datetime();
	User owner();

	void addInvitation(EmailAddress emailAddress);

	void notInterested(User user);
}
