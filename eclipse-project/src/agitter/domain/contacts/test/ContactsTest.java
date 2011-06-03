package agitter.domain.contacts.test;

import java.util.List;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UsersImpl;


public class ContactsTest extends CleanTestBase {

	private final Contacts subject = new ContactsImpl();
	private final User user = jose();

	private User jose() {
		try {
			return new UsersImpl().signup( "jose", "jose@gmail.com", "secret" );
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Test
	public void contactsInAlfabeticalOrder() throws Refusal {
		List<EmailAddress> contacts = subject.contactsFor( user );
		assertTrue( contacts.isEmpty() );
		
		subject.addFor( user, new EmailAddress( "maria@gmail.com" ) );
		subject.addFor( user, new EmailAddress( "joao@gmail.com" ) );
		subject.addFor( user, new EmailAddress( "joao@gmail.com" ) );
		subject.addFor( user, new EmailAddress( "joAO@gmaiL.com" ) );
		subject.addFor( user, new EmailAddress( "amanda@hotmail.com" ) );
		contacts = subject.contactsFor( user );
		assertContents( contacts, new EmailAddress( "amanda@hotmail.com" ), new EmailAddress( "joao@gmail.com" ), new EmailAddress( "maria@gmail.com" ) );
	}
	
}
