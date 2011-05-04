package agitter.domain.events;

import agitter.domain.User;

public interface Event {
	String description();
	long datetime();
	User owner();

	void notInterested(User user);
}
