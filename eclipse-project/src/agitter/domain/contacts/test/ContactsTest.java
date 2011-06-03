package agitter.domain.contacts.test;

import java.util.List;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.contacts.Contacts;
import agitter.domain.contacts.ContactsImpl;
import agitter.domain.contacts.Group;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UsersImpl;


public class ContactsTest extends CleanTestBase {

	private final Contacts subject = new ContactsImpl();

	private final User user = jose();

	
	@Test
	public void contactsInAlfabeticalOrder() throws Refusal {
		List<EmailAddress> contacts = subject.contactsFor( user );
		assertTrue( contacts.isEmpty() );
		
		subject.addContactFor( user, new EmailAddress( "maria@gmail.com" ) );
		subject.addContactFor( user, new EmailAddress( "joao@gmail.com" ) );
		subject.addContactFor( user, new EmailAddress( "joao@gmail.com" ) );
		subject.addContactFor( user, new EmailAddress( "joAO@gmaiL.com" ) );
		subject.addContactFor( user, new EmailAddress( "amanda@hotmail.com" ) );
		contacts = subject.contactsFor( user );
		assertContents( contacts, new EmailAddress( "amanda@hotmail.com" ), new EmailAddress( "joao@gmail.com" ), new EmailAddress( "maria@gmail.com" ) );
	}
	
	@Test
	public void groups() throws Refusal {
		List<Group> groups = subject.groupsFor( user );
		assertTrue( groups.isEmpty() );
		
		subject.addGroupFor( user, "Friends" );
		subject.addGroupFor( user, "Work" );
		subject.addGroupFor( user, "Family" );
		groups = subject.groupsFor( user );
		assertEquals( "[Family, Friends, Work]", groups.toString() );
	}
	
	@Test (expected=Refusal.class)
	public void duplicatedGroup() throws Refusal {
		subject.addGroupFor( user, "Friends" );
		subject.addGroupFor( user, "FRIENDS" );
	}

	private User jose() {
		try {
			return new UsersImpl().signup( "jose", "jose@gmail.com", "secret" );
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}
}
