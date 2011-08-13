package agitter.domain;

import agitter.domain.comments.Comments;
import agitter.domain.comments.CommentsImpl;
import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl2;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl;
import agitter.domain.mailing.Mailing;
import agitter.domain.mailing.MailingImpl;
import agitter.domain.users.Users;
import agitter.domain.users.UsersImpl;


public class AgitterImpl implements Agitter {

	@Deprecated //2011-08-12
	private final Contacts contacts = new agitter.domain.contacts.ContactsImpl();

	private final Users users = new UsersImpl();
	private Contacts contacts2;
	private final Events events = new EventsImpl();
	private final Comments comments = new CommentsImpl();
	private final Mailing mailing = new MailingImpl();
	

	@Override public Users users() { return users; }
	@Override public Contacts contacts() { return contacts2; }
	@Override public Events events() { return events; }
	@Override public Comments comments() { return comments; }
	@Override public Mailing mailing() { return mailing; }
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void migrateSchemaIfNecessary() {
		if (contacts2 == null)
			contacts2 = new ContactsImpl2((agitter.domain.contacts.ContactsImpl)contacts, users);
	}
	
}
