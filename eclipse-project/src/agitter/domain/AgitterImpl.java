package agitter.domain;

import agitter.domain.comments.Comments;
import agitter.domain.comments.CommentsImpl;
import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;
import agitter.domain.mailing.Mailing;
import agitter.domain.mailing.MailingImpl;
import agitter.domain.users.Users;
import agitter.domain.users.UsersImpl;


public class AgitterImpl implements Agitter {

	private final Users users = new UsersImpl();
	private final Contacts contacts = new ContactsImpl();
	private final Events events = new EventsImpl();
	private final Comments comments = new CommentsImpl();
	private final Mailing mailing = new MailingImpl();
	

	@Override public Users users() { return users; }
	@Override public Contacts contacts() { return contacts; }
	@Override public Events events() { return events; }
	@Override public Comments comments() { return comments; }
	@Override public Mailing mailing() { return mailing; }
	
}
