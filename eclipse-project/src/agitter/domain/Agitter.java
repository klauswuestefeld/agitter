package agitter.domain;

import agitter.domain.events.Events;
import agitter.domain.users.Users;

public interface Agitter {

	Users users();
	
	Events events();

}
