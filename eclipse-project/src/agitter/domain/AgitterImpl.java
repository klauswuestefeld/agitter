package agitter.domain;

import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;
import agitter.domain.users.Users;
import agitter.domain.users.UsersImpl;


public class AgitterImpl implements Agitter {

	private final UsersImpl _users = new UsersImpl();
	private final EventsImpl _events = new EventsImpl();


	@Override
	public Events events() {
		return _events;
	}


	@Override
	public Users users() {
		return _users;
	}

	
}
