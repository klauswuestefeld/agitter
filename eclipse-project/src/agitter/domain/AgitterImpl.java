package agitter.domain;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.comments.Comments;
import agitter.domain.comments.CommentsImpl;
import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl2;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Events;
import agitter.domain.events.EventsImpl2;
import agitter.domain.mailing.Mailing;
import agitter.domain.mailing.MailingImpl;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.domain.users.Users.UserNotFound;
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

	private final Comments comments = new CommentsImpl();
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
	}
	
	@Override
	public void mergeAccountsIfNecessary(String email1, String email2) throws UserNotFound, Refusal {
		if (email1 == null || email2 == null) return;
		if (email1.trim().isEmpty() || email2.trim().isEmpty()) return;
		if (email1.trim().equalsIgnoreCase(email2.trim())) return;
		
		try {
			User takingOver = users().findByEmail(EmailAddress.email(email1));
			User beingDropped = users().findByEmail(EmailAddress.email(email2));
		
			if (takingOver.equals(beingDropped)) return;
			
			events().transferEvents(takingOver, beingDropped);
			contacts().transferContacts(takingOver, beingDropped);
			
			users().delete(beingDropped);
		} catch (UserNotFound u) {
			return;
		}
	}
	
}
