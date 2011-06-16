package agitter.domain.events;

import java.util.List;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;

public interface Events {

	@Transaction @Deprecated //Fix the tests to use the new version with invitations below.
	Event create(User user, String description, long datetime) throws Refusal;
	@Transaction
	Event create(User user, String description, long datetime, List<String> invitations) throws Refusal;

	List<Event> toHappen(User user);

}
