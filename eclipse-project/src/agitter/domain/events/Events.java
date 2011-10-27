package agitter.domain.events;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public interface Events {

	@Transaction
	Event create(User user, String description, long datetime, List<Group> inviteeGroups, List<User> invitees) throws Refusal;

	void edit(Event event, String description, long newDatetime, List<Group> inviteeGroups, List<User> newInvitees) throws Refusal;
	
	boolean isDeletableBy(Event event, User user);
	void delete(Event event, User user);

	List<Event> toHappen(User user);

}
