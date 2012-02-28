package agitter.domain.events;

import agitter.domain.users.User;

public interface Occurrence {
	long datetime();	
	void notInterested(User user);
	boolean isInterested(User user);
}
