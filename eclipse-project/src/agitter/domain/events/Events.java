package agitter.domain.events;

import java.util.List;
import java.util.Map;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.emails.EmailDestination;
import agitter.domain.users.User;

public interface Events {

	@Transaction
	Event create(User user, String description, long datetime, List<Group> inviteeGroups, List<EmailAddress> inviteeEmails) throws Refusal;

	List<Event> toHappen(User user);

	Map<EmailDestination, List<Event>> emailsAndItsEventsToHappenIn24Hours();

}
