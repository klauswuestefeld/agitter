package agitter.domain.events;

import java.util.List;
import java.util.SortedSet;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

@Deprecated
public class EventsImpl implements Events {

	SortedSet<EventImpl> _all;

	
	
	@Override
	public Event create(User user, String description, long datetime,
			List<Group> inviteeGroups, List<User> invitees) throws Refusal {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

	@Override
	public List<Event> toHappen(User user) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}

}
