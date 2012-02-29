package agitter.domain.events;

import agitter.domain.users.User;

public interface Occurrence {
	long datetime();	
	void notInterested(User user);
	boolean isInterested(User user);
	
	void going(User user);
	void notGoing(User user);
	void mayGo(User user);
	
	// True -> user is going for sure.
	// False -> user not is going for sure.
	// Null -> user may go. 
	Boolean isGoing(User user);
	boolean hasIgnored(User user);
}
