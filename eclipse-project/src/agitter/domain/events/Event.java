package agitter.domain.events;

import java.util.HashSet;

import agitter.domain.User;

public interface Event {
	String description();
	long datetime();
	void notInterested(User user);
}
