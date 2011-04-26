package agitter.domain;

import agitter.domain.events.Events;

public interface AgitterSession {

	String userName();
	void logout();
	
	Events events();

}
