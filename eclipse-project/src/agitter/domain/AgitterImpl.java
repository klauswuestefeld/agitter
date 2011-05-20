package agitter.domain;

import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;
import agitter.domain.mailing.Mailing;
import agitter.domain.mailing.MailingImpl;
import agitter.domain.users.Users;
import agitter.domain.users.UsersImpl;


public class AgitterImpl implements Agitter {

	private final Users users = new UsersImpl();
	private final Events events = new EventsImpl();
	private final Mailing mailing = new MailingImpl();


	@Override
	public Events events() {
		return events;
	}


	@Override
	public Users users() {
		return users;
	}


	@Override
	public Mailing mailing() {
		return mailing;
	}

	
}
