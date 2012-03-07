package agitter.domain;

import agitter.domain.comments.Comments;
import agitter.domain.comments.CommentsImpl;
import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl2;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl2;
import agitter.domain.mailing.Mailing;
import agitter.domain.mailing.MailingImpl;
import agitter.domain.users.Users;
import agitter.domain.users.UsersImpl;


public class AgitterImpl implements Agitter {

	static private AgitterImpl refToAvoidGc;


	private Object readResolve() {
		if (refToAvoidGc != null) throw new IllegalStateException();
		  refToAvoidGc = this;
		  return this;
	 }

	  
	private final Users users = new UsersImpl();
	private Contacts contacts2 = new ContactsImpl2();
	private Events events2 = new EventsImpl2();

	private Comments comments = new CommentsImpl();
	private final Mailing mailing = new MailingImpl();
	

	@Override public Users users() { return users; }
	@Override public Contacts contacts() { return contacts2; }
	@Override public Events events() { return events2; }
	@Override public Comments comments() { return comments; }
	@Override public Mailing mailing() { return mailing; }
	
	
	{
		migrateSchemaIfNecessary(); //Necessary for instantiating first time. Ex: In tests
	}
	
	
	@Override
	public void migrateSchemaIfNecessary() {
		((EventsImpl2)events2).migrateSchemaIfNecessary();
		
		if (comments == null) comments = new CommentsImpl(); //2012-03-06 Make comments final, after migration. Klaus.
	}
	
}
